from transformers import BertTokenizer, BertModel
import torch
import mysql.connector
import json
import pandas as pd


def clean_text(text):
    text = text.replace('\n', ' ')  # Replace newlines with space
    text = text.replace('\uf0b7', ' ')  # Replace bullet point with space
    text = text.strip()  # Remove extra whitespace
    return text


df = pd.read_excel('./courses_data.xlsx')

courses = []
for idx, row in df.iterrows():
    clean_description = clean_text(row['Περιγραφή'])
    courses.append((row['Μάθημα'], clean_description))

tokenizer = BertTokenizer.from_pretrained('nlpaueb/bert-base-greek-uncased-v1')
model = BertModel.from_pretrained('nlpaueb/bert-base-greek-uncased-v1')

embeddings = []

for name, desc in courses:
    inputs = tokenizer(desc, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    cls_embedding = outputs.last_hidden_state[:, 0, :].squeeze(0)
    embeddings.append((name, cls_embedding.squeeze().numpy()))

conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='root!',
    database='course_recommendation',
    port = 3306
)
cursor = conn.cursor()
for name, embedding in embeddings:
    embedding_str = json.dumps(embedding.tolist())
    cursor.execute('UPDATE courses SET embedding = %s WHERE name = %s', (embedding_str, name))
conn.commit()
cursor.close()
conn.close()
