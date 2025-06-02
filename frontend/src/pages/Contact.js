import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

function Contact() {
    return (
        <div>
            <Navbar />
            <div className="container my-5">
                <h2 className="text-center mb-5">Επικοινωνία</h2>
                <div className="row">

                    {/* Άλσους Αιγάλεω */}
                    <div className="col-md-4 text-center mb-4">
                        <h5>Πανεπιστημιούπολη<br />Άλσους Αιγάλεω</h5>
                        <p>Αγίου Σπυρίδωνος, Αιγάλεω 12243<br />τηλ: 210 5385100</p>
                        <iframe
                            title="Alsos Aigaleo"
                            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3738.757615788051!2d23.671870136471572!3d38.00260920455871!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14a13ba48d426f81%3A0xbe82f5519cbcb53d!2zzqDOsc69zrXPgM65z4PPhM6uzrzOuc6_IM6Uz4XPhM65zrrOrs-CIM6Rz4TPhM65zrrOrs-C!5e0!3m2!1sel!2sgr!4v1748448985233!5m2!1sel!2sgr"
                            width="100%"
                            height="250"
                            style={{ border: 0 }}
                            allowFullScreen=""
                            loading="lazy"
                        ></iframe>
                    </div>

                    {/* Αρχαίου Ελαιώνα */}
                    <div className="col-md-4 text-center mb-4">
                        <h5>Πανεπιστημιούπολη<br />Αρχαίου Ελαιώνα</h5>
                        <p>Π. Ράλλη & Θηβών 250, Αιγάλεω 12241<br />τηλ: 210 5381100</p>
                        <iframe
                            title="Archaio Elaionas"
                            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3144.952685765812!2d23.66895791443929!3d37.978239549166034!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14a1bc9d304b0d59%3A0x4d6cc1b5d5297934!2zzqDOsc69zrXPgM65z4PPhM6uzrzOuc6_IM6Uz4XPhM65zrrOrs-CIM6Rz4TPhM65zrrOrs-CIC0gzqDOsc69zrXPgM65z4PPhM63zrzOuc6_z43PgM6_zrvOtyDOkc-Bz4fOsc6vzr_PhSDOlc67zrHOuc-Ozr3OsQ!5e0!3m2!1sel!2sgr!4v1748448913299!5m2!1sel!2sgr"
                            width="100%"
                            height="250"
                            style={{ border: 0 }}
                            allowFullScreen=""
                            loading="lazy"
                        ></iframe>
                    </div>

                    {/* Αθήνα */}
                    <div className="col-md-4 text-center mb-4">
                        <h5>Πανεπιστημιούπολη<br />Αθηνών</h5>
                        <p>Λεωφόρος Αλεξάνδρας 196, 11521 Αθήνα<br />τηλ: 213 2010100</p>
                        <iframe
                            title="Athens Campus"
                            src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d1572.312712665667!2d23.759302!3d37.985870000000006!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14a1bd54c8c49aaf%3A0x21ffaa8da50488f0!2zzpvOtc-Jz4YuIM6RzrvOtc6-zqzOvc60z4HOsc-CIDE5NiwgzpHOuM6uzr3OsSAxMTUgMjE!5e0!3m2!1sel!2sgr!4v1748448719623!5m2!1sel!2sgr"
                            width="100%"
                            height="250"
                            style={{ border: 0 }}
                            allowFullScreen=""
                            loading="lazy"
                        ></iframe>
                    </div>

                </div>
            </div>
            <Footer />
        </div>
    );
}

export default Contact;