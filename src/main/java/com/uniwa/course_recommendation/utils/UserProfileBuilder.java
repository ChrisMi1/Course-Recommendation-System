package com.uniwa.course_recommendation.utils;

import com.uniwa.course_recommendation.dto.AnswerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class UserProfileBuilder {
    static Logger logger = LoggerFactory.getLogger(UserProfileBuilder.class);

    private UserProfileBuilder() {

    }
//    public static String buildProfileSummary(List<AnswerDto> answers) {
//        logger.info("Building the profile of the user based on the answers");
//        StringBuilder summary = new StringBuilder();
//        summary.append("Ο χρήστης έχει επιλέξει να ακολουθήσει την ροή ");
//        summary.append(answers.get(0).getAnswer());
//        summary.append(".");
//        if (answers.get(0).getAnswer().equals("Λογισμικό και Πληροφοριακά Συστήματα")) {
//            summary.append("Το είδος ανάπτυξης λογισμικού που ενδιαφέρει τον χρήστη είναι ");
//            summary.append(answers.get(1).getAnswer());
//            summary.append(".");
//            switch (answers.get(1).getAnswer()) {
//                case "Άναπτυξη διαδικτυακών εφαρμογών" -> {
//                    summary.append("Το επίπεδο του χρήστη στις γλώσσες προγραμματισμού για ανύπτυξη εφαρμογών είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στις βάσεις δεδομένων και στην sql είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στην σχεδίαση και ανάλυση αλγορίθμων είναι ");
//                    summary.append(answers.get(4).getAnswer());
//                    summary.append(".");
//                }
//                case "Ανάπτυξη παιχνιδιών" -> {
//                    summary.append("Το επίπεδο του χρήστη στα μαθηματικά που χρησιμοποιούνται για την ανάπτυξη παιχνιδιών είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                }
//                case "Ανάπτυξη κινητών εφαρμογών" -> {
//                    summary.append("Το επίπεδο του χρήστη σε γλώσσες προγραμματισμού για κινητά (π.χ. Java/Kotlin για Android, Swift για iOS) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με την SQL για τη διαχείριση της αποθήκευσης δεδομένων εφαρμογών ή τον χειρισμό βάσεων δεδομένων σε εφαρμογές για κινητές συσκευές είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//                case "Τεχνητή νοημοσύνη και μηχανική μάθηση" -> {
//                    summary.append("Το επίπεδο του χρήστη σε γλώσσες προγραμματισμού που χρησιμοποιούνται συνήθως στην τεχνητή νοημοσύνη και τη μηχανική εκμάθηση (π.χ. Python, R ή MATLAB) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στην κατανόησή για τις μαθηματικές έννοιες που είναι απαραίτητες για την τεχνητή νοημοσύνη και τη μηχανική μάθηση, όπως η γραμμική άλγεβρα, ο λογισμός και η στατιστική είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στην κατανόησή για τις βάσεις δεδομένων και την SQL για τη διαχείριση και την αναζήτηση συνόλων δεδομένων που χρησιμοποιούνται στην τεχνητή νοημοσύνη και τη μηχανική μάθηση είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//                default -> {
//                    summary.append("Το επίπεδο του χρήστη στην κατανόηση των βάσεων δεδομένων και της SQL για τη διαχείριση και την υποβολή ερωτημάτων σε σύνολα δεδομένων είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στα τα μαθηματικά που χρησιμοποιούνται συνήθως στην επιστήμη των δεδομένων (π.χ. λογισμός, γραμμική άλγεβρα) είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//            }
//        } else if (answers.get(0).getAnswer().equals("Συστήματα Ροής Υλικών και Υπολογιστών")) {
//            summary.append("Ο τομέας μηχανικού υλικού που ενδιαφέρει τον χρήστη είναι ");
//            summary.append(answers.get(1).getAnswer());
//            summary.append(".");
//            switch (answers.get(1).getAnswer()) {
//                case "Ενσωματωμένα συστήματα" -> {
//                    summary.append("Το επίπεδο του χρήστη στην γλώσσα C/C++ είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στις αρχές σχεδίασης κυκλωμάτων είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//                case "Ρομποτική" -> {
//                    summary.append("Το επίπεδο του χρήστη σε γλώσσες που χρησιμοποιούνται συνήθως στη ρομποτική (π.χ. Python, C/C++ ή MATLAB) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στις αρχές σχεδίασης κυκλωμάτων είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//                case "Μικροϋπολογιστές" -> {
//                    summary.append("Το επίπεδο του χρήστη με γλώσσες χαμηλού επιπέδου που χρησιμοποιούνται συνήθως σε μικροϋπολογιστές (π.χ. Assembly, C) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τις έννοιες της ψηφιακής ηλεκτρονικής που σχετίζονται με τους μικροϋπολογιστές (π.χ. λογικές πύλες, δυαδικές λειτουργίες) είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//                default -> {
//                    summary.append("Το επίπεδο του χρήστη για το σχεδιασμό ψηφιακής λογικής (π.χ. συνδυαστικά και διαδοχικά κυκλώματα) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη για τα MOSFET και τον ρόλο τους στα ολοκληρωμένα κυκλώματα είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με το VHDL ή τη Verilog για το σχεδιασμό και την προσομοίωση ψηφιακών κυκλωμάτων είναι ");
//                    summary.append(answers.get(4).getAnswer());
//                    summary.append(".");
//                }
//
//            }
//        } else {
//            summary.append("Το είδος δικτύωσης που ενδιαφέρει τον χρήστη είναι ");
//            summary.append(answers.get(1).getAnswer());
//            summary.append(".");
//            switch (answers.get(1).getAnswer()) {
//                case "Σχεδιασμός Δικτύου" -> {
//                    summary.append("Το επίπεδο του χρήστη με την κατανόηση της διεύθυνσης IP και του υποδικτύου είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με το σχεδιασμό ενός απλού τοπικού δικτύου (LAN) είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη  με τη χρήση λογισμικού προσομοίωσης δικτύου ή σχεδιασμού (π.χ. Cisco Packet Tracer, GNS3, Wireshark) είναι ");
//                    summary.append(answers.get(4).getAnswer());
//                    summary.append(".");
//
//                }
//                case "Ασφάλεια Δικτύου" -> {
//                    summary.append("Το επίπεδο του χρήστη με τις τεχνικές και τα πρωτόκολλα κρυπτογράφησης (π.χ. SSL/TLS, AES, RSA) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη στο σχεδιασμού και της τμηματοποίησης ασφαλούς δικτύου είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τη χρήση εργαλείων ασφάλειας δικτύου (π.χ. Wireshark, Snort, Nmap) για την παρακολούθηση και την ασφάλεια των δικτύων είναι ");
//                    summary.append(answers.get(4).getAnswer());
//                    summary.append(".");
//                }
//                case "Cloud Computing" -> {
//                    summary.append("Το επίπεδο του χρήστη με το Secure Sockets Layer (SSL) και το Transport Layer Security (TLS) ως πρωτόκολλα δικτύου για την ασφάλεια της μετάδοσης δεδομένων cloud είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τις βασικές μετρήσεις δικτύωσης (π.χ. εύρος ζώνης, καθυστέρηση) και τον αντίκτυπό τους στην απόδοση του cloud είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                }
//                case "Ασύρματα δίκτυα και IoT" -> {
//                    summary.append("Το επίπεδο του χρήστη με τις βασικές έννοιες δικτύωσης όπως η διεύθυνση IP , η τοπολογία δικτύου και οι μετρήσεις δικτύου (π.χ. καθυστέρηση, απόδοση, ισχύς σήματος) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τα ενσωματωμένα συστήματα, συγκεκριμένα τους μικροελεγκτές και τους αισθητήρες είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τις προγραμματιστικές  δεξιότητες σε γλώσσες που χρησιμοποιούνται συνήθως για το IoT, όπως η C ή η Python είναι ");
//                    summary.append(answers.get(4).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με θέματα ασφάλειας που αφορούν συγκεκριμένα δίκτυα IoT (π.χ. κρυπτογράφηση, έλεγχος ταυτότητας, ακεραιότητα δεδομένων) είναι ");
//                    summary.append(answers.get(5).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη σχετικά με τις πιθανότητες και τα στατιστικά στοιχεία, ιδιαίτερα στο πλαίσιο της ανάλυσης της αξιοπιστίας και της απόδοσης των δεδομένων IoT είναι ");
//                    summary.append(answers.get(6).getAnswer());
//                    summary.append(".");
//                }
//                default -> {
//                    summary.append("Το επίπεδο του χρήστη με τις τηλεπικοινωνιακές υποδομές και τα πρωτόκολλα (π.χ. IP, TCP/IP, LTE) είναι ");
//                    summary.append(answers.get(2).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με την επεξεργασία σήματος και τη μετάδοση δεδομένων μέσω διαφόρων μέσων (π.χ. οπτικές ίνες, ραδιοκύματα) είναι ");
//                    summary.append(answers.get(3).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τις τεχνικές διαμόρφωσης και αποδιαμόρφωσης, όπως AM, FM και ψηφιακή διαμόρφωση είναι ");
//                    summary.append(answers.get(4).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τις πιθανότητες και τα στατιστικά είναι ");
//                    summary.append(answers.get(5).getAnswer());
//                    summary.append(".");
//                    summary.append("Το επίπεδο του χρήστη με τα ψηφιακά και αναλογικά συστήματα, συμπεριλαμβανομένων των μεθόδων μετατροπής σήματος και δειγματοληψίας είναι ");
//                    summary.append(answers.get(6).getAnswer());
//                    summary.append(".");
//                }
//
//            }
//        }
//        return summary.toString();
//    }

    public static String buildProfileSummary(List<AnswerDto> answers) {
        StringBuilder summary = new StringBuilder();
        summary.append("Ο χρήστης ενδιαφέρεται για την ροή " + answers.get(0).getAnswer() + " καθώς και για την ειδίκευση " + answers.get(1).getAnswer() + ".");
        summary.append("Σαν επιπλέον ενδιαφέροντα έχει ");
        Arrays.stream(answers.get(2).getAnswer().split("\\|")).forEach(ans -> summary.append(ans));
        summary.append("Ο χρήστης δυσκολεύεται με τα κατώθι ");
        for (int i=3; i<answers.size(); i++) {
            Arrays.stream(answers.get(i).getAnswer().split("\\|")).forEach(ans -> summary.append(ans));
        }
        return summary.toString();
    }
}
