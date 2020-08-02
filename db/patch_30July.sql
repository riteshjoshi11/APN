use antrackerdb;
CREATE TABLE `p_txn_reports` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgid` int(11) NOT NULL,
  `pdffilepath` varchar(256) DEFAULT NULL,
  `excelfilepath` varchar(256) DEFAULT NULL,
  `generatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `generatedby` varchar(150) DEFAULT NULL,
  `reportstatus` varchar(20) DEFAULT NULL,
  `period` varchar(20) DEFAULT NULL,
  `type`   varchar(20) DEFAULT NULL,
  `format` varchar(20) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_txn_reports_ibfk_1` (`orgid`),
  CONSTRAINT `p_txn_reports_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table will store the transaction related process data for all the organization'



CREATE TABLE `p_txnrpt_send_email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `p_txn_reports_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_txnrpt_send_email_fk` (`p_txn_reports_id`),
  CONSTRAINT `p_txnrpt_send_email_fk` FOREIGN KEY (`p_txn_reports_id`) REFERENCES `p_txn_reports` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This is a process table which will maintain a email sent of Transaction report.'


ALTER TABLE `antrackerdb`.`employee` 
CHANGE COLUMN `currentsalarybalance` `currentsalarybalance` FLOAT NULL DEFAULT 0 ,
CHANGE COLUMN `lastsalarybalance` `lastsalarybalance` FLOAT NULL DEFAULT 0 ;
