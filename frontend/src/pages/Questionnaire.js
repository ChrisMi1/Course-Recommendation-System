import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { AnimatePresence, motion } from 'framer-motion';
import logo from '../assets/logo.png';
import jsPDF from 'jspdf';
import RobotoRegular from '../assets/fonts/Roboto-Regular';
import Spline from '@splinetool/react-spline';

function Questionnaire() {
  const [allQuestions, setAllQuestions] = useState([]);
  const [questionQueue, setQuestionQueue] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [userAnswers, setUserAnswers] = useState([]);
  const [isCompleted, setIsCompleted] = useState(false);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [showRecommendations, setShowRecommendations] = useState(false);
  const [expandedLessons, setExpandedLessons] = useState({});
  const [loadingRecommendations, setLoadingRecommendations] = useState(false);

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

  useEffect(() => {
    if (currentQuestion) {
      setSelectedAnswer(currentQuestion.type === 'multi' ? [] : null);
    }
  }, [currentQuestion]);

  const handleAnswerSelect = (answerText) => {
    if (!currentQuestion) return;

    if (currentQuestion.type === 'multi') {
      setSelectedAnswer(prev =>
        prev.includes(answerText)
          ? prev.filter(a => a !== answerText)
          : [...prev, answerText]
      );
    } else {
      setSelectedAnswer(answerText);
    }
  };

  const handleNextClick = () => {
    if (!selectedAnswer || (Array.isArray(selectedAnswer) && selectedAnswer.length === 0)) {
      alert('Please select at least one answer first.');
      return;
    }

    const answerToSave = Array.isArray(selectedAnswer)
      ? selectedAnswer.join('|')
      : selectedAnswer;

    const newAnswer = {
      questionId: currentQuestion.id,
      question: currentQuestion.question,
      answer: answerToSave,
    };

    const updatedAnswers = [...userAnswers, newAnswer];
    setUserAnswers(updatedAnswers);

    const newQueue = [...questionQueue];

    const processAnswers = (selAns) => {
      const answerObj = currentQuestion.answers.find(ans => ans.answer === selAns);
      if (answerObj?.nextQuestionId) {
        const ids = answerObj.nextQuestionId
          .split(',')
          .map(id => parseInt(id.trim(), 10))
          .filter(id => !isNaN(id));
        newQueue.push(...ids);
      }
    };

    if (currentQuestion.type === 'multi') {
      selectedAnswer.forEach(processAnswers);
    } else {
      processAnswers(selectedAnswer);
    }

    const uniqueQueue = [...new Set(newQueue)];

    while (uniqueQueue.length > 0) {
      const nextId = uniqueQueue.shift();
      const nextQuestion = allQuestions.find(q => q.id === nextId);
      if (nextQuestion) {
        setQuestionQueue(uniqueQueue);
        setCurrentQuestion(nextQuestion);
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
      setLoadingRecommendations(true);
      await axios.post('http://localhost:8080/answers', answersToSend, { withCredentials: true });
      const recoResponse = await axios.get('http://localhost:8080/recommendations', { withCredentials: true });
      setRecommendations(recoResponse.data);
    } catch (error) {
      console.error('Failed to submit answers:', error);
    } finally {
      setLoadingRecommendations(false);
    }
  };

  const toggleLesson = (id) => {
    setExpandedLessons(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const handleShowRecommendations = () => {
    setShowRecommendations(true);
    setTimeout(() => {
      document.getElementById("recommendationsSection")?.scrollIntoView({ behavior: "smooth" });
    }, 100);
  };

  const downloadPDF = () => {
    const doc = new jsPDF();
    doc.addFileToVFS("Roboto-Regular.ttf", RobotoRegular);
    doc.addFont("Roboto-Regular.ttf", "Roboto", "normal");
    doc.setFont("Roboto");
    doc.setFontSize(16);

    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    const imgWidth = 100;
    const imgHeight = 100;
    const x = (pageWidth - imgWidth) / 2;
    const y = (pageHeight - imgHeight) / 2;

    doc.addImage(logo, 'PNG', x, y, imgWidth, imgHeight, '', 'FAST');
    doc.text("Recommended Lessons", 10, 10);

    recommendations.forEach((course, index) => {
      const y = 20 + index * 15;
      doc.setFontSize(14);
      doc.text(`${index + 1}. ${course.name}`, 10, y);
      doc.setFontSize(11);
      doc.text(`URL: ${course.url}`, 12, y + 6);
    });

    doc.save('recommended-lessons.pdf');
  };

  return (
    <div className="full-blur-wrapper">
      {loadingRecommendations && (
        <>
          <div className="blur-overlay"></div>
          <div className="spline-logo-wrapper">
            <Spline scene="/scene.splinecode" />
          </div>
        </>
      )}

      <header className="bg-primary text-white text-center py-5 position-relative">
        <img src={logo} alt="Logo" className="centered-logo" />
        <div className="container">
          <h1 className="display-4">Questionnaire</h1>
          <p className="lead">Answer the questions below:</p>
        </div>
      </header>

      <section className="container my-5">
        {isCompleted ? (
          <div className="text-center my-5">
            <h4>Το ερωτηματολόγιο ολοκληρώθηκε! 🎉</h4>
            <div className="d-flex justify-content-center gap-3 mt-4 flex-wrap">
              <button className="btn btn-primary" onClick={handleShowRecommendations}>
                Εμφάνιση Αποτελεσμάτων
              </button>
              {showRecommendations && (
                <button className="btn btn-outline-secondary" onClick={downloadPDF}>
                  Download PDF
                </button>
              )}
            </div>

            {showRecommendations && (
              <div id="recommendationsSection">
                <h5 className="mt-5">Οι προτεινόμενες προτάσεις μαθημάτων:</h5>

                {[
                  { title: 'Υποχρεωτικά Μαθήματα', filter: c => c.mandatory, color: 'primary', icon: 'check-circle-fill' },
                  { title: 'Προαπαιτούμενα Μαθήματα', filter: c => c.prerequest, color: 'warning', icon: 'arrow-right-circle' },
                  { title: 'Επιλεγόμενα Μαθήματα', filter: c => !c.mandatory && !c.prerequest, color: 'success', icon: 'star-fill' }
                ].map(({ title, filter, color, icon }) => {
                  const courses = recommendations.filter(filter);
                  if (courses.length === 0) return null;
                  return (
                    <div key={title} className="mt-4 section-box">
                      <h6 className={`fw-bold text-${color} d-flex align-items-center`}>
                        <i className={`bi bi-${icon} me-2`} /> {title}:
                      </h6>
                      {courses.map(course => (
                        <div className="card my-2 shadow-sm text-start" key={course.id}>
                          <div className="card-body d-flex align-items-center justify-content-start gap-3">
                            <div className="flex-grow-1">
                              <h6 className="mb-0">
                                <a
                                  href={course.url}
                                  target="_blank"
                                  rel="noopener noreferrer"
                                  className="text-decoration-none fw-bold text-dark"
                                  style={{ fontSize: '1rem' }}
                                >
                                  {course.name}
                                </a>
                              </h6>
                            </div>
                            {(!course.mandatory && !course.prerequest) && (
                              <button
                                className="btn btn-sm btn-outline-secondary"
                                onClick={() => toggleLesson(course.id)}
                              >
                                {expandedLessons[course.id] ? '︿' : '﹀'}
                              </button>
                            )}
                          </div>
                          <div
                            className={`collapse-content overflow-hidden px-3 text-start ${expandedLessons[course.id] ? 'expanded' : ''}`}
                          >
                            <div
                              className="small text-muted mb-0"
                              style={{
                                whiteSpace: 'pre-line',
                                maxHeight: '150px',
                                overflowY: 'auto',
                                paddingRight: '4px',
                              }}
                            >
                              {course.explanation}
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        ) : currentQuestion ? (
          <div className="question-card-container">
            <AnimatePresence mode="wait">
              <motion.div
                key={currentQuestion.id}
                initial={{ x: 300, opacity: 0 }}
                animate={{ x: 0, opacity: 1 }}
                exit={{ x: -300, opacity: 0 }}
                transition={{ duration: 0.4 }}
                className="card p-4 shadow mb-4 question-card"
              >
                <h5>{currentQuestion.question}</h5>
                <div className="mt-3 d-grid gap-2">
                  {currentQuestion.answers
                    .filter(ans => ans.answer !== null)
                    .map((ans, index) => (
                      <button
                        key={index}
                        className={`btn m-2 ${currentQuestion.type === 'multi'
                          ? selectedAnswer.includes(ans.answer)
                            ? 'btn-primary'
                            : 'btn-outline-primary'
                          : selectedAnswer === ans.answer
                            ? 'btn-primary'
                            : 'btn-outline-primary'
                          }`}
                        onClick={() => handleAnswerSelect(ans.answer)}
                      >
                        {ans.answer}
                      </button>
                    ))}
                </div>
                <div className="next-btn-container mt-4 d-flex justify-content-center">
                  <button className="btn btn-success" onClick={handleNextClick}>
                    Επόμενο
                  </button>
                </div>
              </motion.div>
            </AnimatePresence>
          </div>
        ) : (
          <div className="d-flex justify-content-center">
            <p>Loading...</p>
          </div>

        )}
      </section>
    </div>
  );
}

export default Questionnaire;
