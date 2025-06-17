import React, { useState, useEffect, useRef } from 'react';
import logo1 from '../assets/logo1.png';
import logo2 from '../assets/logo2.png';
import logo3 from '../assets/logo3.png';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { useNavigate } from 'react-router-dom';

function Home() {
  const navigate = useNavigate();
  const images = [logo1, logo2, logo3];
  const imagesLoop = [...images, images[0]]; // for looping

  const [currentIndex, setCurrentIndex] = useState(0);
  const sliderRef = useRef(null);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prev) => prev + 1);
    }, 4000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    if (currentIndex === imagesLoop.length - 1) {
      const timer = setTimeout(() => {
        if (sliderRef.current) {
          sliderRef.current.style.transition = 'none';
          setCurrentIndex(0);
          sliderRef.current.style.transform = `translateX(0%)`;
          void sliderRef.current.offsetWidth; // force reflow
          sliderRef.current.style.transition = 'transform 1s ease-in-out';
        }
      }, 1000);
      return () => clearTimeout(timer);
    }
  }, [currentIndex, imagesLoop.length]);

  return (
    <div className="page-wrapper">
      <Navbar />

      <main className="flex-grow-1">
        <header className="bg-primary home-header text-white text-center py-5">
          <div className="container">
            <h1 className="display-4">Course Recommendation System</h1>
            <p className="lead">Πατήστε το κουμπί για να ξεκινήσετε!</p>
            <button
              onClick={() => navigate('/questionnaire')}
              className="btn btn-light btn-lg mt-3"
            >
              Start Questionnaire
            </button>
          </div>

          {/* Background slider */}
          <div className="slider-wrapper">
            <div
              className="slider-track"
              ref={sliderRef}
              style={{
                transform: `translateX(-${currentIndex * 100}%)`,
                transition: 'transform 1s ease-in-out',
              }}
            >
              {imagesLoop.map((img, i) => (
                <img
                  key={i}
                  src={img}
                  alt={`Feature ${i + 1}`}
                  className="slider-image"
                  draggable={false}
                />
              ))}
            </div>
          </div>
        </header>
      </main>

      <Footer />
    </div>
  );
}

export default Home;
