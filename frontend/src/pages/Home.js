import feature1 from '../assets/feature1.png';
import feature2 from '../assets/feature2.png';
import feature3 from '../assets/feature3.png';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

function Home() {
  return (
    <div>
      <Navbar />

      {/* Hero Section */}
      <header className="bg-primary text-white text-center py-5">
        <div className="container">
          <h1 className="display-4">Course Recommendation System</h1>
          <p className="lead">Πατήστε το κουμπί για να ξεκινήσετε!</p>
          <a href="questionnaire.html" className="btn btn-light btn-lg mt-3">
            Start Questionnaire
          </a>
        </div>
      </header>

      {/* Features Section */}
      <section className="py-5">
        <div className="container">
          <div className="row g-4">
            <div className="col-md-4 text-center">
              <img src={feature1} alt="Feature 1" className="mb-3" width="150" />
              <h5>Quick</h5>
              <p>Finish in just 5 minutes.</p>
            </div>
            <div className="col-md-4 text-center">
              <img src={feature2} alt="Feature 2" className="mb-3" width="150" />
              <h5>Anonymous</h5>
              <p>Your answers are 100% private.</p>
            </div>
            <div className="col-md-4 text-center">
              <img src={feature3} alt="Feature 3" className="mb-3" width="150" />
              <h5>Insightful</h5>
              <p>Get personalized insights.</p>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
}

export default Home;
