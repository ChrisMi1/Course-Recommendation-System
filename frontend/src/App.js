import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import About from './pages/About';
import Questionnaire from './pages/Questionnaire';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/questionnaire" element={<Questionnaire/>} />
      </Routes>
    </Router>
  );
}

export default App;
