use antrackerdb;

drop table attendance;
CREATE TABLE `attendance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(25) NOT NULL,
  `attendancemethod` varchar(30) NOT NULL,
  `signaturetype` varchar(30) NOT NULL,
  `hoursworked` int(11) DEFAULT NULL,
  `orgid` int(11) NOT NULL,
  `otherdetails` varchar(45) DEFAULT NULL,
  `signaturetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `attendance_ibfk_1_idx` (`orgid`),
  KEY `attendance_ibfk_2_idx` (`employeeid`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

drop table facerecognition;
CREATE TABLE `facerecognition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(45) NOT NULL,
  `orgid` int(11) NOT NULL,
  `face` blob NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `employeeid_UNIQUE` (`employeeid`),
  KEY `facerecognition_ibfk_2_idx` (`orgid`),
  CONSTRAINT `facerecognition_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `facerecognition_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

drop table fingerprint;
CREATE TABLE `fingerprint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(25) NOT NULL,
  `orgid` int(11) NOT NULL,
  `print` blob NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EmployeeId_UNIQUE` (`employeeid`),
  KEY `fingerprint_ibfk_1_idx` (`orgid`),
  CONSTRAINT `fingerprint_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `fingerprint_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
