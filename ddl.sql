CREATE SCHEMA IF NOT EXISTS course_recommendations;

USE course_recommendations; 

CREATE TABLE `course` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(300) NOT NULL,
    `code` NVARCHAR(50) NOT NULL,
    `description` MEDIUMTEXT NOT NULL,
    PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

GRANT FILE ON *.* TO 'root'@'localhost';
SET GLOBAL local_infile=1;
load data local infile "C:\\Users\\xrist\\Downloads\\courses_data.csv" INTO TABLE course_recommendations.course
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(`name`,`code`,`description`) ; 


 show global variables like 'local_infile';

