import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import About from './pages/About';
// import Results from './pages/Results';
import Contact from './pages/Contact';
import Questionnaire from './pages/Questionnaire';
import './App.css';
import ChatPopup from './components/ChatPopup';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/questionnaire" element={<Questionnaire/>} />
        {/* <Route path="/results" element={<Results/>} /> */}
        <Route path="/contact" element={<Contact/>} />
      </Routes>
      <ChatPopup /> 
    </Router>
  );
}

export default App;
