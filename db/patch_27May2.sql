DROP TABLE `antrackerdb`.`calculationtracker`;

CREATE TABLE `calculationtracker` (
  `orgid` int NOT NULL AUTO_INCREMENT,
  `unpaidexpense` float DEFAULT '0',
  `paidexpense` float DEFAULT '0',
  `totalexpense` float GENERATED ALWAYS AS ((`unpaidexpense` + `paidexpense`)) VIRTUAL,
  `totalpaidsalary` float DEFAULT '0',
  KEY `orgid` (`orgid`),
  CONSTRAINT `CalculationTracker_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci