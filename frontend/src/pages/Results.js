import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

function Results() {
  return (
    <div>
      <Navbar />
      <div className="text-center mt-5">
        <h1>Course Recommendation System</h1>
        <p className="mt-3">Καλώς ήρθατε στην σελίδα μας. Αυτή η σελίδα δημιουργήθηκε για μια
             σωστή καθοδήγηση στην επιλογή των μαθημάτων που θα παρακολουθήσει ο φοιτητής</p>
      </div>
      <Footer />
    </div>
  );
}

export default Results;