import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Questionnaire() {
  const [allQuestions, setAllQuestions] = useState([]);
  const [questionQueue, setQuestionQueue] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [userAnswers, setUserAnswers] = useState([]);
  const [isCompleted, setIsCompleted] = useState(false);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [showRecommendations, setShowRecommendations] = useState(false);



  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await axios.get('http://localhost:8080/questions', { withCredentials: true });
        setAllQuestions(response.data);

        const firstQuestion = response.data.find(q => q.id === 1);
        if (firstQuestion) {
          setCurrentQuestion(firstQuestion);
          setIsCompleted(false);
        }
      } catch (error) {
        console.error('Failed to fetch questions:', error);
      }
    };

    fetchQuestions();
  }, []);


  const handleAnswerSelect = (nextIdsString, selectedAnswerText) => {
    setSelectedAnswer({ nextIdsString, selectedAnswerText });
  };


  const handleNextClick = () => {
    if (!selectedAnswer) {
      alert('Please select an answer first.');
      return;
    }

    const { nextIdsString, selectedAnswerText } = selectedAnswer;
    //ΜΠΟΡΕΙ ΝΑ ΓΡΑΦΕΙ ΚΑΙ ΕΤΣΙ 
    //const nextIdsString = selectedAnswer.nextIdsString;
    //const selectedAnswerText = selectedAnswer.selectedAnswerText;

    const newAnswer = {
      questionId: currentQuestion.id,
      question: currentQuestion.question,
      answer: selectedAnswerText
    };

    const updatedAnswers = [...userAnswers, newAnswer];
    setUserAnswers(updatedAnswers);

    const newQueue = [...questionQueue];
    if (nextIdsString) {
      const nextIds = nextIdsString
        .split(',')
        .map(id => parseInt(id.trim(), 10))
        .filter(id => !isNaN(id));
      newQueue.push(...nextIds);
    }

    while (newQueue.length > 0) {
      const nextId = newQueue.shift();
      const nextQuestion = allQuestions.find(q => q.id === nextId);
      if (nextQuestion) {
        setQuestionQueue(newQueue);
        setCurrentQuestion(nextQuestion);
        setSelectedAnswer(null);
        return;
      }
    }


    setQuestionQueue([]);
    setCurrentQuestion(null);
    setIsCompleted(true);
    submitAnswersToBackend(updatedAnswers);
  };


  const submitAnswersToBackend = async (answersToSend) => {
    try {
      const response = await axios.post('http://localhost:8080/answers', answersToSend, { withCredentials: true });
      console.log('Answers submitted:', response.data);

      // Now fetch recommendations
      const recoResponse = await axios.get('http://localhost:8080/recommendations', { withCredentials: true });
      setRecommendations(recoResponse.data);
    } catch (error) {
      console.error('Failed to submit answers:', error);
    }
  };

  return (
    <div>
      <header className="bg-primary text-white text-center py-5">
        <div className="container">
          <h1 className="display-4">Questionnaire</h1>
          <p className="lead">Answer the questions below:</p>
        </div>
      </header>

      <section className="container my-5">
        {isCompleted ? (
          <div className="text-center my-5">
            <h4>Το ερωτηματολόγιο ολοκληρώθηκε! 🎉</h4>

            <button
              className="btn btn-primary mt-4"
              onClick={() => setShowRecommendations(prev => !prev)}
            >
              Eμφάνιση Αποτελεσμάτων
            </button>

            {showRecommendations && (
              <>
                <h5 className="mt-5">Οι προτεινόμενες προτάσεις μαθημάτων:</h5>
                {recommendations.length > 0 ? (
                  <ul className="list-group mt-3">
                    {recommendations.map((course) => (
                      <li
                        key={course.id}
                        className="list-group-item d-flex justify-content-between align-items-center"
                      >
                        <span>{course.name}</span>
                        <span className="badge bg-primary rounded-pill">
                          {course.similarity.toFixed(2)}
                        </span>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>Δεν υπάρχουν προτάσεις προς το παρόν.</p>
                )}
              </>
            )}
          </div>
        ) : currentQuestion ? (
          <div key={currentQuestion.id} className="card p-4 shadow mb-4">
            <h5>{currentQuestion.question}</h5>
            <div className="mt-3">
              {currentQuestion.answers
                .filter(ans => ans.answer !== null)
                .map((ans, index) => (
                  <button
                    key={index}
                    className={`btn m-2 ${selectedAnswer?.selectedAnswerText === ans.answer
                      ? 'btn-primary'
                      : 'btn-outline-primary'
                      }`}
                    onClick={() => handleAnswerSelect(ans.nextQuestionId, ans.answer)}
                  >
                    {ans.answer}
                  </button>
                ))}
            </div>

            <div className="mt-4">
              <button className="btn btn-success" onClick={handleNextClick}>
                Next Question
              </button>
            </div>
          </div>
        ) : (
          <div className="text-center my-5">
            <h4>Loading...</h4>
          </div>
        )}
      </section>
    </div>
  );
}

export default Questionnaire;
