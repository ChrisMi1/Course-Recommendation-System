// Updated Questionnaire.js with smooth expand/collapse for main tabs (full height)

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { AnimatePresence, motion } from 'framer-motion';
import logo from '../assets/logo.png';
import jsPDF from 'jspdf';
import RobotoRegular from '../assets/fonts/Roboto-Regular';
import Spline from '@splinetool/react-spline';
import Navbar from '../components/Navbar';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

const InfoTooltip = ({ text }) => (
  <OverlayTrigger
    placement="right"
    overlay={<Tooltip>{text}</Tooltip>}
  >
    <span className="ms-2 info-icon" role="button">ⓘ</span>
  </OverlayTrigger>
);


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
  const [activeFlowTab, setActiveFlowTab] = useState('Όλα');
  const [expandedSections, setExpandedSections] = useState({});
  const [errorMessage, setErrorMessage] = useState('');


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
        prev.includes(answerText) ? prev.filter(a => a !== answerText) : [...prev, answerText]
      );
    } else {
      setSelectedAnswer(answerText);
    }
  };

  const handleNextClick = () => {
    if (!selectedAnswer || (Array.isArray(selectedAnswer) && selectedAnswer.length === 0)) {
      setErrorMessage('Παρακαλώ επιλέξτε τουλάχιστον μία απάντηση.');
      return;
    }

    setErrorMessage('');

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
      setShowRecommendations(true);
      setTimeout(() => {
        document.getElementById("recommendationsSection")?.scrollIntoView({ behavior: "smooth" });
      }, 100);
    } catch (error) {
      console.error('Failed to submit answers:', error);
    } finally {
      setLoadingRecommendations(false);
    }
  };

  const toggleLesson = (id) => {
    setExpandedLessons(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const toggleSection = (title) => {
    setExpandedSections(prev => {
      const isExpanding = !prev[title];
      const updated = { ...prev, [title]: isExpanding };

      if (isExpanding) {
        // Delay scroll so it happens after expand animation starts
        setTimeout(() => {
          const el = document.getElementById(`${title}-section`);
          if (el) {
            el.scrollIntoView({ behavior: 'smooth', block: 'start' });
          }
        }, 400); // Slightly after framer-motion transition (0.5s)
      }

      return updated;
    });
  };


  const downloadPDF = () => {
    const doc = new jsPDF();
    doc.addFileToVFS("Roboto-Regular.ttf", RobotoRegular);
    doc.addFont("Roboto-Regular.ttf", "Roboto", "normal");
    doc.setFont("Roboto");

    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();

    let y = 20;

    const drawLogoBackground = () => {
      doc.addImage(logo, 'PNG', (pageWidth - 100) / 2, (pageHeight - 100) / 2, 100, 100, '', 'FAST', 0.1);
    };

    drawLogoBackground(); // First page

    doc.setFontSize(18);
    doc.text("Προτεινόμενα Μαθήματα", 10, y);
    y += 10;

    const sections = [
      {
        title: 'Επιλεγόμενα Μαθήματα',
        filter: c => !c.mandatory && !c.prerequest,
      },
      {
        title: 'Βασικής Ροής Μαθήματα',
        filter: c => c.mandatory,
      },
      {
        title: 'Προαπαιτούμενα Μαθήματα',
        filter: c => c.prerequest,
      }
    ];

    sections.forEach(section => {
      const courses = recommendations.filter(section.filter);
      if (courses.length === 0) return;

      if (y > 260) {
        doc.addPage();
        drawLogoBackground();
        y = 20;
      }

      doc.setFontSize(15);
      doc.text(section.title, 10, y);
      y += 8;

      courses.forEach(course => {
        if (y > 270) {
          doc.addPage();
          drawLogoBackground();
          y = 20;
        }

        doc.setFontSize(13);
        doc.text(`• ${course.name}`, 12, y);
        y += 5;

        doc.setFontSize(10);
        doc.text(`URL: ${course.url}`, 14, y);
        y += 6;
      });
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
      <Navbar />
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
              {showRecommendations && (
                <button className="btn btn-outline-secondary" onClick={downloadPDF}>
                  Download PDF
                </button>
              )}
            </div>

            {showRecommendations && (
              <div id="recommendationsSection">
                <h5 className="mt-5">Οι προτεινόμενες προτάσεις μαθημάτων:</h5>

                {[{
                  title: 'Επιλεγόμενα Μαθήματα',
                  filter: c => !c.mandatory && !c.prerequest,
                  color: 'success',
                  icon: 'star-fill'
                }, {
                  title: 'Βασικής Ροής Μαθήματα',
                  filter: c => c.mandatory,
                  color: 'primary',
                  icon: 'check-circle-fill'
                }, {
                  title: 'Προαπαιτούμενα Μαθήματα',
                  filter: c => c.prerequest,
                  color: 'warning',
                  icon: 'arrow-right-circle'
                }].map(({ title, filter, color, icon }) => {
                  const isExpanded = expandedSections[title];
                  return (
                    <div key={title} className="mt-4 section-box" id={`${title}-section`}>
                      <button className={`btn ${isExpanded ? 'btn-secondary text-white' : 'btn-light'} fw-bold d-flex justify-content-between align-items-center w-100 text-start`} onClick={() => toggleSection(title)}>
                        <span className="d-flex align-items-center gap-1">
                          <i className={`bi bi-${icon}`} />
                          {title}
                          {title === 'Επιλεγόμενα Μαθήματα' && (
                            <InfoTooltip text="Μαθήματα που προτάθηκαν βάσει των ενδιαφερόντων σου." />
                          )}
                          {title === 'Βασικής Ροής Μαθήματα' && (
                            <InfoTooltip text="Υποχρεωτικά μαθήματα που σχετίζονται με την κατεύθυνσή σου." />
                          )}
                          {title === 'Προαπαιτούμενα Μαθήματα' && (
                            <InfoTooltip text="Βασικά μαθήματα που προτείνονται βάσει των αδυναμιών σου για ενίσχυση." />
                          )}
                        </span>

                        <span>{isExpanded ? '︿' : '﹀'}</span>
                      </button>

                      {isExpanded && title === 'Επιλεγόμενα Μαθήματα' && (
                        <div className="d-flex flex-wrap my-3">
                          {['Όλα', 'Ροή Λογισμικού και Πληροφοριακών Συστημάτων', 'Ροή Υλικού και Υπολογιστικών Συστημάτων', 'Ροή Δικτύων Υπολογιστών και Επικοινωνιών'].map(flowName => (
                            <button
                              key={flowName}
                              className={`btn btn-sm me-2 mb-2 ${activeFlowTab === flowName ? 'btn-secondary text-white' : 'btn-light'}`}
                              onClick={() => setActiveFlowTab(flowName)}
                            >
                              {flowName}
                            </button>
                          ))}
                        </div>
                      )}

                      <AnimatePresence initial={false}>
                        {isExpanded && (
                          <motion.div
                            key="content"
                            initial={{ height: 0, opacity: 0 }}
                            animate={{ height: 'auto', opacity: 1 }}
                            exit={{ height: 0, opacity: 0 }}
                            transition={{ duration: 0.5, ease: 'easeInOut' }}
                            style={{ overflow: 'hidden' }}
                          >
                            {recommendations
                              .filter(filter)
                              .filter(c => title !== 'Επιλεγόμενα Μαθήματα' || activeFlowTab === 'Όλα' || c.flow === activeFlowTab)
                              .map(course => (
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
                                    {title === 'Επιλεγόμενα Μαθήματα' && (
                                      <button className="btn btn-sm btn-outline-secondary" onClick={() => toggleLesson(course.id)}>
                                        {expandedLessons[course.id] ? '︿' : '﹀'}
                                      </button>
                                    )}
                                  </div>
                                  <div className={`collapse-content overflow-hidden px-3 text-start ${expandedLessons[course.id] ? 'expanded' : ''}`}>
                                    <div className="small text-muted mb-0" style={{ whiteSpace: 'pre-line', maxHeight: '150px', overflowY: 'auto', paddingRight: '4px' }}>
                                      {course.explanation}
                                    </div>
                                  </div>
                                </div>
                              ))}
                          </motion.div>
                        )}
                      </AnimatePresence>
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
                {errorMessage && (
                  <div className="alert alert-warning alert-dismissible fade show" role="alert">
                    {errorMessage}
                    <button
                      type="button"
                      className="btn-close"
                      onClick={() => setErrorMessage('')}
                      aria-label="Close"
                    ></button>
                  </div>
                )}
                <h5 className="mb-0">
                  {currentQuestion.question}
                  <span className="text-muted small ms-2">
                    ({currentQuestion.type === 'multi' ? 'πολλαπλή επιλογή' : 'μονή επιλογή'})
                  </span>
                </h5>

                <div className="mt-3 d-grid gap-2">
                  {currentQuestion.answers.filter(ans => ans.answer !== null).map((ans, index) => (
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
