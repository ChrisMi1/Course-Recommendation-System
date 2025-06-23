import React, { useState } from 'react';
import axios from 'axios';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import Spline from '@splinetool/react-spline';

function Results() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    const userMessage = input.trim();
    if (!userMessage) return;

    setMessages((prev) => [...prev, { sender: 'user', text: userMessage }]);
    setInput('');
    setLoading(true);

    try {
      const response = await axios.get(`http://localhost:8080/chatbot?messageUser=${encodeURIComponent(userMessage)}`);


      const assistantReply = response.data || '[No response]';


      setMessages((prev) => [...prev, { sender: 'bot', text: assistantReply }]);
    } catch (err) {
      console.error(err);
      setMessages((prev) => [
        ...prev,
        { sender: 'bot', text: '❌ Error: Backend communication failed.' },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="full-blur-wrapper">
      {loading && (
        <>
          <div className="blur-overlay"></div>
          <div className="spline-logo-wrapper">
            <Spline scene="/scene.splinecode" />
          </div>
        </>
      )}

      <div className="content">
        <Navbar />
        <div className="text-center mt-5 px-4">
          <h1 className="text-2xl font-bold">Course Chatbot Assistant</h1>
          <p className="mt-3 max-w-2xl mx-auto">
            Ask anything about your courses, study plan, or recommendations. The assistant knows the lesson content.
          </p>
        </div>

        <div className="mt-10 flex justify-center">
          <div className="chatbot max-w-xl w-full bg-white p-4 shadow rounded-lg">
            <div className="chat-box h-96 overflow-y-auto mb-4 border p-2">
              {messages.map((msg, idx) => (
                <div
                  key={idx}
                  className={`mb-2 text-${msg.sender === 'user' ? 'right' : 'left'}`}
                >
                  <div
                    className={`inline-block px-3 py-2 rounded ${
                      msg.sender === 'user' ? 'bg-blue-100' : 'bg-gray-200'
                    }`}
                  >
                    {msg.text}
                  </div>
                </div>
              ))}
            </div>
            <div className="flex">
              <input
                className="flex-grow border rounded-l px-3 py-2"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="Ask something about your courses..."
              />
              <button
                className="bg-blue-500 text-white px-4 rounded-r"
                onClick={handleSubmit}
                disabled={loading}
              >
                {loading ? '...' : 'Send'}
              </button>
            </div>
          </div>
        </div>

        <Footer />
      </div>
    </div>
  );
}

export default Results;
