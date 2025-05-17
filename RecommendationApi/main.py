import pickle

import redis as redis
from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel
from transformers import BertTokenizer, BertModel
from sklearn.metrics.pairwise import cosine_similarity
import torch
import numpy as np
from sqlalchemy import create_engine, text
import ast

model_name = 'nlpaueb/bert-base-greek-uncased-v1'
tokenizer = BertTokenizer.from_pretrained(model_name)
model = BertModel.from_pretrained(model_name)

app = FastAPI()

engine = create_engine("mysql+pymysql://root:root%21@localhost/course_recommendation")
redis_client = redis.Redis(host='localhost', port=6379, db=0)

class SummaryRequest(BaseModel):
    summary: str

def get_embedding(text: str):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    return outputs.last_hidden_state[:, 0, :].numpy().squeeze()  # [CLS] token

def fetch_courses():
    cache_key = "courses_cache"
    cached_data = redis_client.get(cache_key)
    if cached_data:
        return pickle.loads(cached_data)
    with engine.connect() as conn:
        result = conn.execute(text("SELECT id, name, embedding FROM courses"))
        courses = []
        for row in result:
            emb = np.array(ast.literal_eval(row.embedding))
            courses.append({
                "id": row.id,
                "name": row.name,
                "embedding": emb
            })
        redis_client.setex(cache_key, 3600, pickle.dumps(courses))
        return courses

@app.post("/recommendations")
def get_recommendation(request: SummaryRequest):
    try:
        user_embedding = get_embedding(request.summary)
        courses = fetch_courses()

        similarities = []
        for course in courses:
            sim = cosine_similarity([user_embedding], [course['embedding']])[0][0]
            similarities.append((course['id'], course['name'], sim))

        # Sort and take top 10
        top_courses = sorted(similarities, key=lambda x: x[2], reverse=True)[:10]

        return [
            {"id": cid, "name": name, "similarity": float(sim)} for cid, name, sim in top_courses
        ]

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


