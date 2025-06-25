import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import './ChatPopup.css'; // προαιρετικό αν διαχωρίσεις CSS

function ChatPopup() {
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const [loading, setLoading] = useState(false);
    const chatBodyRef = useRef(null);

    useEffect(() => {
        if (chatBodyRef.current) {
            chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
        }
    }, [messages, loading]);

    const togglePopup = () => setIsOpen(!isOpen);

    const handleSubmit = async () => {
        const userMessage = input.trim();
        if (!userMessage) return;

        setMessages(prev => [...prev, { sender: 'user', text: userMessage }]);
        setInput('');
        setLoading(true);

        try {
            const response = await axios.get(`http://localhost:8080/chatbot?messageUser=${encodeURIComponent(userMessage)}`);
            const reply = response.data;
            setMessages(prev => [...prev, { sender: 'bot', text: reply }]);
        } catch {
            setMessages(prev => [...prev, { sender: 'bot', text: 'Right now i am not availiable.' }]);
        } finally {
            setLoading(false);
        }


    };

    return (
        <div className="chat-popup-wrapper">
            <button className="chat-button" onClick={togglePopup}>
                💬
            </button>

            {isOpen && (
                <div className="chat-popup">
                    <div className="chat-header">
                        <span>Chat Assistant</span>
                        <button className="close-btn" onClick={togglePopup}>×</button>
                    </div>
                    <div className="chat-body" ref={chatBodyRef}>
                        {messages.map((msg, i) => (
                            <div key={i} className={`chat-bubble ${msg.sender}`}>
                                {msg.text}
                            </div>
                        ))}
                        {loading && (
                            <div className="chat-bubble bot typing-indicator">
                                <span></span><span></span><span></span>
                            </div>
                        )}

                    </div>
                    <div className="chat-input">
                        <input
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && handleSubmit()}
                            placeholder="Type a message..."
                        />
                        <button onClick={handleSubmit}>Send</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default ChatPopup;
