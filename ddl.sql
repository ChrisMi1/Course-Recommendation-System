DROP SCHEMA IF EXISTS `course_recommendation`;

CREATE SCHEMA IF NOT EXISTS `course_recommendation`;

USE `course_recommendation`; 

CREATE TABLE `courses` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(300) NOT NULL,
    `code` NVARCHAR(50) NOT NULL,
    `description` MEDIUMTEXT NOT NULL,
    `embedding` MEDIUMTEXT NOT NULL,
	`inserted_at` datetime NOT NULL DEFAULT current_timestamp(),
	`inserted_by` varchar(100) NOT NULL DEFAULT 'system',
	`modified_at` datetime NOT NULL DEFAULT current_timestamp(),
	`modified_by` varchar(100) DEFAULT NULL,
	`row_version` decimal(22,0) NOT NULL DEFAULT 1,
	PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `questions` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
    `question` VARCHAR(1000) NOT NULL, 
    `options` VARCHAR(1000) NOT NULL, 
	`inserted_at` datetime NOT NULL DEFAULT current_timestamp(),
	`inserted_by` varchar(100) NOT NULL DEFAULT 'system',
	`modified_at` datetime NOT NULL DEFAULT current_timestamp(),
	`modified_by` varchar(100) DEFAULT NULL,
	`row_version` decimal(22,0) NOT NULL DEFAULT 1,
    PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `question_rules` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `question_id` INT NOT NULL, 
    `answer_value` VARCHAR(400), 
    `next_question_id` INT,
    `question_sequence` INT,
	`inserted_at` datetime NOT NULL DEFAULT current_timestamp(),
	`inserted_by` varchar(100) NOT NULL DEFAULT 'system',
	`modified_at` datetime NOT NULL DEFAULT current_timestamp(),
	`modified_by` varchar(100) DEFAULT NULL,
	`row_version` decimal(22,0) NOT NULL DEFAULT 1,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`question_id`) REFERENCES `questions`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user_answers` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `session_id` VARCHAR(150) NOT NULL,
    `track_id` VARCHAR(36) NOT NULL,
    `question_id` INT NOT NULL, 
    `answer` VARCHAR(400),
    `inserted_at` datetime NOT NULL DEFAULT current_timestamp(),
	`inserted_by` varchar(100) NOT NULL DEFAULT 'system',
	`modified_at` datetime NOT NULL DEFAULT current_timestamp(),
	`modified_by` varchar(100) DEFAULT NULL,
	`row_version` decimal(22,0) NOT NULL DEFAULT 1,
	PRIMARY KEY(`id`),
    FOREIGN KEY(`question_id`) REFERENCES `questions`(`id`) 
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

GRANT FILE ON *.* TO 'root'@'localhost';
SET GLOBAL local_infile=1;
load data local infile "C:\\Users\\xrist\\Downloads\\courses_data.csv" INTO TABLE course_recommendation.courses
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(`name`,`code`,`description`) ; 

INSERT INTO `questions` (`question`,`options`) VALUES ('Σε ποια ροή εξειδίκευσης θέλετε να εστιάσετε; ','Λογισμικό και Πληροφοριακά Συστήματα,Συστήματα Ροής Υλικών και Υπολογιστών,Δίκτυο Υπολογιστών και Επικοινωνιών') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Για ποιο είδος ανάπτυξης λογισμικού σας ενδιαφέρει περισσότερο;','Άναπτυξη διαδικτυακών εφαρμογών,Ανάπτυξη παιχνιδιών,Ανάπτυξη κινητών εφαρμογών,Τεχνητή νοημοσύνη και μηχανική μάθηση ,Επιστήμη Δεδομένων') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την επάρκειά σας σε γλώσσες προγραμματισμού που χρησιμοποιούνται συνήθως στην ανάπτυξη διαδικτυακών εφαρμογών;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με τις έννοιες της SQL και της βάσης δεδομένων;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με το σχεδιασμό και την ανάλυση αλγορίθμων;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόησή σας για τα μαθηματικά που χρησιμοποιούνται συνήθως στην ανάπτυξη παιχνιδιών (π.χ. διανύσματα, πίνακες, γραμμική άλγεβρα); ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την επάρκειά σας σε γλώσσες προγραμματισμού για κινητά (π.χ. Java/Kotlin για Android, Swift για iOS);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο άνετα αισθάνεστε με την SQL για τη διαχείριση της αποθήκευσης δεδομένων εφαρμογών ή τον χειρισμό βάσεων δεδομένων σε εφαρμογές για κινητές συσκευές;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την επάρκειά σας σε γλώσσες προγραμματισμού που χρησιμοποιούνται συνήθως στην τεχνητή νοημοσύνη και τη μηχανική εκμάθηση (π.χ. Python, R ή MATLAB);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόησή σας για τις μαθηματικές έννοιες που είναι απαραίτητες για την τεχνητή νοημοσύνη και τη μηχανική μάθηση, όπως η γραμμική άλγεβρα, ο λογισμός και η στατιστική;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε την κατανόησή σας για τις βάσεις δεδομένων και την SQL για τη διαχείριση και την αναζήτηση συνόλων δεδομένων που χρησιμοποιούνται στην τεχνητή νοημοσύνη και τη μηχανική μάθηση;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόηση των βάσεων δεδομένων και της SQL για τη διαχείριση και την υποβολή ερωτημάτων σε σύνολα δεδομένων; ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόησή σας για τα μαθηματικά που χρησιμοποιούνται συνήθως στην επιστήμη των δεδομένων (π.χ. λογισμός, γραμμική άλγεβρα); ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Ποιος τομέας της μηχανικής υλικού σας ενδιαφέρει περισσότερο; ','Ενσωματωμένα συστήματα,Ρομποτική,Μικροϋπολογιστές,Σχεδιασμός VLSI') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με τη C/C++;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόηση των αρχών σχεδίασης κυκλωμάτων; ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε τις προγραμματιστικές σας δεξιότητες, ειδικά σε γλώσσες που χρησιμοποιούνται συνήθως στη ρομποτική (π.χ. Python, C/C++ ή MATLAB);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόηση των αρχών σχεδίασης κυκλωμάτων;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την εμπειρία προγραμματισμού σας με γλώσσες χαμηλού επιπέδου που χρησιμοποιούνται συνήθως σε μικροϋπολογιστές (π.χ. Assembly, C);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο άνετα αισθάνεστε με τις έννοιες της ψηφιακής ηλεκτρονικής που σχετίζονται με τους μικροϋπολογιστές (π.χ. λογικές πύλες, δυαδικές λειτουργίες);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόησή σας για το σχεδιασμό ψηφιακής λογικής (π.χ. συνδυαστικά και διαδοχικά κυκλώματα);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε τις γνώσεις σας για τα MOSFET και τον ρόλο τους στα ολοκληρωμένα κυκλώματα;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο άνετα είστε με το VHDL ή τη Verilog για το σχεδιασμό και την προσομοίωση ψηφιακών κυκλωμάτων;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Ποιο είδος δικτύωσης σας ενδιαφέρει περισσότερο;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόηση των πρωτοκόλλων δικτύου (π.χ. TCP/IP, UDP, HTTP, FTP);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε την κατανόηση της διεύθυνσης IP και του υποδικτύου;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο άνετα είστε με το σχεδιασμό ενός απλού τοπικού δικτύου (LAN); ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με τη χρήση λογισμικού προσομοίωσης δικτύου ή σχεδιασμού (π.χ. Cisco Packet Tracer, GNS3, Wireshark);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με τις τεχνικές και τα πρωτόκολλα κρυπτογράφησης (π.χ. SSL/TLS, AES, RSA);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε την κατανόηση του σχεδιασμού και της τμηματοποίησης ασφαλούς δικτύου;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο άνετα αισθάνεστε με τη χρήση εργαλείων ασφάλειας δικτύου (π.χ. Wireshark, Snort, Nmap) για την παρακολούθηση και την ασφάλεια των δικτύων;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Είστε εξοικειωμένοι με το Secure Sockets Layer (SSL) και το Transport Layer Security (TLS) ως πρωτόκολλα δικτύου για την ασφάλεια της μετάδοσης δεδομένων cloud;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με τις βασικές μετρήσεις δικτύωσης (π.χ. εύρος ζώνης, καθυστέρηση) και τον αντίκτυπό τους στην απόδοση του cloud; ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την κατανόησή σας για βασικές έννοιες δικτύωσης όπως η διεύθυνση IP , η τοπολογία δικτύου και οι μετρήσεις δικτύου (π.χ. καθυστέρηση, απόδοση, ισχύς σήματος);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την εμπειρία σας με τα ενσωματωμένα συστήματα, συγκεκριμένα τους μικροελεγκτές και τους αισθητήρες;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε τις προγραμματιστικές σας δεξιότητες σε γλώσσες που χρησιμοποιούνται συνήθως για το IoT, όπως η C ή η Python;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πόσο εξοικειωμένοι είστε με θέματα ασφάλειας που αφορούν συγκεκριμένα δίκτυα IoT (π.χ. κρυπτογράφηση, έλεγχος ταυτότητας, ακεραιότητα δεδομένων); ','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε τις γνώσεις σας σχετικά με τις πιθανότητες και τα στατιστικά στοιχεία, ιδιαίτερα στο πλαίσιο της ανάλυσης της αξιοπιστίας και της απόδοσης των δεδομένων IoT;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα βαθμολογούσατε την εξοικείωσή σας με τις τηλεπικοινωνιακές υποδομές και τα πρωτόκολλα (π.χ. IP, TCP/IP, LTE);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Ποια είναι η εμπειρία σας με την επεξεργασία σήματος και τη μετάδοση δεδομένων μέσω διαφόρων μέσων (π.χ. οπτικές ίνες, ραδιοκύματα);','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε τις γνώσεις σας για τεχνικές διαμόρφωσης και αποδιαμόρφωσης, όπως AM, FM και ψηφιακή διαμόρφωση;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Ποια είναι η εξοικείωσή σας με τις πιθανότητες και τα στατιστικά;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;
INSERT INTO `questions` (`question`,`options`) VALUES ('Πώς θα αξιολογούσατε τις γνώσεις σας για ψηφιακά και αναλογικά συστήματα, συμπεριλαμβανομένων των μεθόδων μετατροπής σήματος και δειγματοληψίας;','Αρχάριος,Βασικό,Ενδιάμεσος,Προχωρημένος,Ειδικός') ;


INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`) VALUES (1,'Λογισμικό και Πληροφοριακά Συστήματα',2); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Άναπτυξη διαδικτυακών εφαρμογών',3,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Άναπτυξη διαδικτυακών εφαρμογών',4,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Άναπτυξη διαδικτυακών εφαρμογών',5,30); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Ανάπτυξη παιχνιδιών',6,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Ανάπτυξη κινητών εφαρμογών',7,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Ανάπτυξη κινητών εφαρμογών',8,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Τεχνητή νοημοσύνη και μηχανική μάθηση',9,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Τεχνητή νοημοσύνη και μηχανική μάθηση',10,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Τεχνητή νοημοσύνη και μηχανική μάθηση',11,30); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Επιστήμη Δεδομένων',12,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (2,'Επιστήμη Δεδομένων',13,20);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`) VALUES (1,'Συστήματα Ροής Υλικών και Υπολογιστών',14); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Ενσωματωμένα συστήματα',15,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Ενσωματωμένα συστήματα',16,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Ρομποτική',17,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Ρομποτική',18,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Μικροϋπολογιστές',19,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Μικροϋπολογιστές',20,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Σχεδιασμός VLSI',21,10); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Σχεδιασμός VLSI',22,20);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (14,'Σχεδιασμός VLSI',23,30);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`) VALUES (1,'Δίκτυο Υπολογιστών και Επικοινωνιών',24);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Σχεδιασμός Δικτύου',25,10);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Σχεδιασμός Δικτύου',26,20);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Σχεδιασμός Δικτύου',27,30);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Σχεδιασμός Δικτύου',28,40);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασφάλεια Δικτύου',29,10);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασφάλεια Δικτύου',30,20); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασφάλεια Δικτύου',31,30); 
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Cloud Computing',32,10);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Cloud Computing',33,20);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασύρματα δίκτυα και IoT',34,10);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασύρματα δίκτυα και IoT',35,20);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασύρματα δίκτυα και IoT',36,30);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασύρματα δίκτυα και IoT',37,40);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Ασύρματα δίκτυα και IoT',38,50);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Τηλεπικοινωνίες',39,10);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Τηλεπικοινωνίες',40,20);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Τηλεπικοινωνίες',41,30);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Τηλεπικοινωνίες',42,40);
INSERT INTO `question_rules` (`question_id`,`answer_value`,`next_question_id`,`question_sequence`) VALUES (24,'Τηλεπικοινωνίες',43,50);













 








 
 







































