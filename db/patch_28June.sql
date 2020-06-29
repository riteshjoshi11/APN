use antrackerdb;
SET SQL_SAFE_UPDATES = 0;
DROP TABLE IF EXISTS employeetype;


CREATE TABLE `employeetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(75) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO employeetype VALUES (5,'accountant'),(4,'labour'),(1,'owner'),(2,'partner'),(3,'sales');

ALTER TABLE `antrackerdb`.`employee`
CHANGE COLUMN `type` `type` INT NULL DEFAULT NULL ;

ALTER TABLE `antrackerdb`.`employee`
ADD CONSTRAINT `employee_ibfk_2`
  FOREIGN KEY (`type`)
  REFERENCES `antrackerdb`.`employeetype` (`int`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;