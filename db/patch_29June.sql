use antrackerdb;
SET SQL_SAFE_UPDATES = 0;
DROP TABLE IF EXISTS orgdetails;
DROP TABLE IF EXISTS companytype;
DROP TABLE IF EXISTS noofemployee;
DROP TABLE IF EXISTS businessnature;

CREATE TABLE `companytype` (
  `id` int NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `noofemployee` (
  `id` int(11) NOT NULL,
  `range` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `businessnature` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


  CREATE TABLE `orgdetails` (
  `id` int(11) NOT NULL,
  `mobile1` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile2` varchar(15) DEFAULT NULL,
  `gstnumber` varchar(20) DEFAULT NULL,
  `pannumber` varchar(15) DEFAULT NULL,
  `comapnytype` int(11) DEFAULT NULL,
  `businessnature` int(11) DEFAULT NULL,
  `numberofemployees` int(11) DEFAULT NULL,
  `extradetails` varchar(500) DEFAULT NULL,
  `accountdetails` varchar(500) DEFAULT NULL,
  `billingaddress` varchar(800) DEFAULT NULL,
  `caname` varchar(80) DEFAULT NULL,
  `caemail` varchar(255) DEFAULT NULL,
  `camobile` varchar(15) DEFAULT NULL,
  `orgid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orgdetails_ibfk_1_idx` (`orgid`),
  KEY `orgdetails_ibfk_2_idx` (`numberofemployees`),
  KEY `orgdetails_ibfk_3_idx` (`businessnature`),
  KEY `orgdetails_ibfk_4_idx` (`comapnytype`),
  CONSTRAINT `orgdetails_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `orgdetails_ibfk_2` FOREIGN KEY (`numberofemployees`) REFERENCES `noofemployee` (`id`),
  CONSTRAINT `orgdetails_ibfk_3` FOREIGN KEY (`businessnature`) REFERENCES `businessnature` (`id`),
  CONSTRAINT `orgdetails_ibfk_4` FOREIGN KEY (`comapnytype`) REFERENCES `companytype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

