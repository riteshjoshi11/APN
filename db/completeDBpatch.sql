DROP DATABASE IF EXISTS test_antracker_db;
CREATE DATABASE test_antracker_db;
use test_antracker_db;

DROP procedure IF EXISTS `ArchiveAndPurgeProcess_Procedure`;
DROP procedure IF EXISTS `ControlOrgDataGrowthDateWise_Procedure`;
DROP procedure IF EXISTS `ControlOrgDataGrowthTransactionWise_Procedure`;
DROP procedure IF EXISTS `InternalDataControl_Procedure`;
DROP procedure IF EXISTS `CustomerVendorBalanceTracking_Procedure`;
DROP procedure IF EXISTS `EmployeeSalaryDueTotal_Procedure`;
DROP procedure IF EXISTS `Restore_SoftDeletedOrgData`;
DROP procedure IF EXISTS `SoftDeleteAuditData_Procedure`;
DROP procedure IF EXISTS `SoftDeleteOrgData_Procedure`;
DROP procedure IF EXISTS `TotalCashInHand_Procedure`;
DROP procedure IF EXISTS `UpdateCustomerBalanceWithAudit_Procedure`;
DROP procedure IF EXISTS `UpdateEmployeeBalanceWithAudit_Procedure`;
DROP procedure IF EXISTS `UpdateEmployeeSalaryBalanceWithAudit_Procedure`;

DROP function IF EXISTS `getcustomerId`;
DROP function IF EXISTS `getEmployeeId`;


Drop TABLE if EXISTS `customer_seq`;
Drop TABLE if EXISTS `customeraudit`;
Drop TABLE if EXISTS `businessnature`;
Drop TABLE if EXISTS `calculationtracker`;
Drop TABLE if EXISTS `companytype`;
Drop TABLE if EXISTS `city`;
Drop TABLE if EXISTS `customeraudit_archive`;
Drop TABLE if EXISTS `customerinvoice`;
Drop TABLE if EXISTS `customerinvoice_archive`;
Drop TABLE if EXISTS `delivery`;
Drop TABLE if EXISTS `delivery_archive`;
Drop TABLE if EXISTS `employee_salary_audit`;
Drop TABLE if EXISTS `employee_seq`;
Drop TABLE if EXISTS `employeeaudit`;
Drop TABLE if EXISTS `employeeaudit_archive`;
Drop TABLE if EXISTS `employeesalary`;
Drop TABLE if EXISTS `employeesalary_archive`;
Drop TABLE if EXISTS `employeesalarypayment`;
Drop TABLE if EXISTS `employeesalarypayment_archive`;
Drop TABLE if EXISTS `expense_cat`;
Drop TABLE if EXISTS `generalexpense`;
Drop TABLE if EXISTS `generalexpense_archive`;
Drop TABLE if EXISTS `internaltransfer`;
Drop TABLE if EXISTS `internaltransfer_archive`;
Drop TABLE if EXISTS `noofemployee`;
Drop TABLE if EXISTS `org_subscription`;
Drop TABLE if EXISTS `organization`;
Drop TABLE if EXISTS `orgdetails`;
Drop TABLE if EXISTS `otphandler`;
Drop TABLE if EXISTS `p_gst_reports`;
Drop TABLE if EXISTS `p_gstrpt_send_email`;
Drop TABLE if EXISTS `p_txn_reports`;
Drop TABLE if EXISTS `p_txnrpt_send_email`;
Drop TABLE if EXISTS `paymentreceived`;
Drop TABLE if EXISTS `paymentreceived_archive`;
Drop TABLE if EXISTS `paytovendor`;
Drop TABLE if EXISTS `paytovendor_archive`;
Drop TABLE if EXISTS `permission`;
Drop TABLE if EXISTS `phonebook`;
Drop TABLE if EXISTS `phonebook_contact`;
Drop TABLE if EXISTS `purchasefromvendor`;
Drop TABLE if EXISTS `purchasefromvendor_archive`;
Drop TABLE if EXISTS `retailsale`;
Drop TABLE if EXISTS `retailsale_archive`;
Drop TABLE if EXISTS `role_permission`;
Drop TABLE if EXISTS `systemconfigurations`;
Drop TABLE if EXISTS `employee`;
Drop TABLE if EXISTS `customer`;
Drop TABLE if EXISTS `account`;
Drop TABLE if EXISTS `organization`;
Drop TABLE if EXISTS `employeetype`;






CREATE TABLE `organization` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orgname` varchar(200) NOT NULL,
  `state` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `clientid` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ownerid` varchar(25) DEFAULT NULL,
  `accountnickname` varchar(300) DEFAULT NULL,
  `type` varchar(20) NOT NULL,
  `details` varchar(20) DEFAULT NULL,
  `orgid` int NOT NULL,
  `createdbyid` varchar(25) DEFAULT NULL,
  `currentbalance` decimal(10,2) DEFAULT '0.00',
  `lastbalance` decimal(10,2) DEFAULT '0.00',
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `initialbalance` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `orgid` (`orgid`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customer` (
  `id` varchar(25) NOT NULL,
  `name` varchar(200) NOT NULL,
  `city` varchar(200) DEFAULT NULL,
  `gstin` varchar(200) DEFAULT NULL,
  `transporter` varchar(200) DEFAULT NULL,
  `mobile1` varchar(20) DEFAULT NULL,
  `mobile2` varchar(20) DEFAULT NULL,
  `firmname` varchar(500) DEFAULT NULL,
  `billingadress` varchar(500) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(50) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sendPaymentReminders` tinyint(1) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uqfirmnameid` (`orgid`,`firmname`,`name`),
  UNIQUE KEY `uqmob1id` (`orgid`,`mobile1`),
  UNIQUE KEY `uqmob2id` (`orgid`,`mobile2`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employeetype` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(75) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `employee` (
  `id` varchar(25) NOT NULL,
  `first` varchar(50) NOT NULL,
  `last` varchar(50) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `loginrequired` tinyint(1) DEFAULT NULL,
  `type` INT DEFAULT NULL,
  `currentsalarybalance` decimal(10,2) DEFAULT '0.00',
  `lastsalarybalance` decimal(10,2) DEFAULT '0.00',
  `orgid` INT NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mobile2` varchar(20) DEFAULT NULL,
  `initialsalarybalance` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uqnameid` (`orgid`,`first`,`last`),
  UNIQUE KEY `uqmobid` (`orgid`,`mobile`),
  KEY `employee_ibfk_2` (`type`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`type`) REFERENCES `employeetype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `customer_seq` (
  `id` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customeraudit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `customerid` varchar(45) DEFAULT NULL,
  `accountid` INT DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `operation` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` decimal(10,2) DEFAULT NULL,
  `previousbalance` decimal(10,2) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `orgid` bigint(20) NOT NULL,
  `txndate` datetime DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customerid` (`customerid`),
  KEY `accountid` (`accountid`),
  CONSTRAINT `customeraudit_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customer` (`id`),
  CONSTRAINT `customeraudit_ibfk_2` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the custome balance update audit function';


CREATE TABLE `businessnature` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `calculationtracker` (
  `orgid` INT NOT NULL AUTO_INCREMENT,
  `unpaidexpense` decimal(10,2) DEFAULT '0.00',
  `paidexpense` decimal(10,2) DEFAULT '0.00',
  `totalexpense` decimal(10,2) GENERATED ALWAYS AS ((`unpaidexpense` + `paidexpense`)) VIRTUAL,
  `totalpaidsalary` decimal(10,2) DEFAULT '0.00',
  KEY `orgid` (`orgid`),
  CONSTRAINT `CalculationTracker_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `companytype` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `city` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uqcity` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `customeraudit_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `customerid` varchar(45) DEFAULT NULL,
  `accountid` INT DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `operation` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` decimal(10,2) DEFAULT NULL,
  `previousbalance` decimal(10,2) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `orgid` bigint(20) NOT NULL,
  `txndate` datetime DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customerid` (`customerid`),
  KEY `accountid` (`accountid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the custome balance update audit function';

CREATE TABLE `customerinvoice` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tocustomerid` varchar(25) DEFAULT NULL,
  `date` timestamp NOT NULL,
  `orderamount` decimal(10,2) NOT NULL,
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `igst` decimal(10,2) DEFAULT '0.00',
  `extra` decimal(10,2) DEFAULT '0.00',
  `totalamount` decimal(10,2) DEFAULT '0.00',
  `note` varchar(256) DEFAULT NULL,
  `invoiceno` varchar(100) DEFAULT NULL,
  `toaccountid` INT DEFAULT NULL,
  `orgid` INT NOT NULL,
  `includeinreport` tinyint(1) DEFAULT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tocustomerid` (`tocustomerid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `customerinvoice_ibfk_1` FOREIGN KEY (`tocustomerid`) REFERENCES `customer` (`id`),
  CONSTRAINT `customerinvoice_ibfk_2` FOREIGN KEY (`toaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `customerinvoice_ibfk_3` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `customerinvoice_ibfk_4` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customerinvoice_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tocustomerid` varchar(25) DEFAULT NULL,
  `date` timestamp NOT NULL,
  `orderamount` decimal(10,2) NOT NULL,
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `igst` decimal(10,2) DEFAULT '0.00',
  `extra` decimal(10,2) DEFAULT '0.00',
  `totalamount` decimal(10,2) DEFAULT '0.00',
  `note` varchar(256) DEFAULT NULL,
  `invoiceno` varchar(100) DEFAULT NULL,
  `toaccountid` INT DEFAULT NULL,
  `orgid` INT NOT NULL,
  `includeinreport` tinyint(1) DEFAULT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tocustomerid` (`tocustomerid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `delivery` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL,
  `tocustomerid` varchar(25) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tocustomerid` (`tocustomerid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `delivery_ibfk_1` FOREIGN KEY (`tocustomerid`) REFERENCES `customer` (`id`),
  CONSTRAINT `delivery_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `delivery_ibfk_3` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `delivery_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL,
  `tocustomerid` varchar(25) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tocustomerid` (`tocustomerid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employee_salary_audit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(45) DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `operation` varchar(45) DEFAULT NULL,
  `otherparty` decimal(10,2) DEFAULT NULL,
  `newbalance` decimal(10,2) DEFAULT NULL,
  `previousbalance` decimal(10,2) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `orgid` INT NOT NULL,
  `txndate` datetime DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employeeid` (`employeeid`),
  KEY `employee_salary_audit_ibfk_2` (`orgid`),
  CONSTRAINT `employee_salary_audit_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `employee_salary_audit_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the employee Salary balance update';

CREATE TABLE `employee_seq` (
  `id` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employeeaudit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(45) DEFAULT NULL,
  `accountid` INT DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` INT DEFAULT NULL,
  `operation` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` decimal(10,2) DEFAULT NULL,
  `previousbalance` decimal(10,2) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `orgid` bigint(20) NOT NULL,
  `forwhat` varchar(30) DEFAULT NULL,
  `txndate` datetime DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employeeid` (`employeeid`),
  KEY `accountid` (`accountid`),
  CONSTRAINT `employeeaudit_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `employeeaudit_ibfk_2` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the employee balance update audit function';

CREATE TABLE `employeeaudit_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(45) DEFAULT NULL,
  `accountid` INT DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` INT DEFAULT NULL,
  `operation` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` decimal(10,2) DEFAULT NULL,
  `previousbalance` decimal(10,2) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `orgid` bigint(20) NOT NULL,
  `forwhat` varchar(30) DEFAULT NULL,
  `txndate` datetime DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employeeid` (`employeeid`),
  KEY `accountid` (`accountid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the employee balance update audit function';

CREATE TABLE `employeesalary` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `toemployeeid` varchar(25) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `employeesalary_ibfk_1` FOREIGN KEY (`toemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `employeesalary_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `employeesalary_ibfk_3` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employeesalary_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `toemployeeid` varchar(25) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employeesalarypayment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT DEFAULT NULL,
  `transferdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `toemployeeid` varchar(25) NOT NULL,
  `fromemployeeid` varchar(25) NOT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `employeesalarypayment_ibfk_1` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `employeesalarypayment_ibfk_2` FOREIGN KEY (`fromemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `employeesalarypayment_ibfk_3` FOREIGN KEY (`toemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `employeesalarypayment_ibfk_4` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `employeesalarypayment_ibfk_5` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `employeesalarypayment_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT DEFAULT NULL,
  `transferdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `toemployeeid` varchar(25) NOT NULL,
  `fromemployeeid` varchar(25) NOT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `expense_cat` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `generalexpense` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category` varchar(200) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `totalamount` decimal(10,2) NOT NULL,
  `topartyname` varchar(200) DEFAULT NULL,
  `fromaccountid` INT NOT NULL,
  `fromemployeeid` varchar(25) NOT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `includeinreport` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  `orderamount` decimal(10,2) DEFAULT '0.00',
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `igst` decimal(10,2) DEFAULT '0.00',
  `extra` decimal(10,2) DEFAULT '0.00',
  `topartygstno` varchar(20) DEFAULT NULL,
  `topartymobileno` varchar(15) DEFAULT NULL,
  `paid` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `generalexpense_ibfk_1` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `generalexpense_ibfk_2` FOREIGN KEY (`fromemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `generalexpense_ibfk_3` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `generalexpense_ibfk_4` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `generalexpense_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category` varchar(200) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `totalamount` decimal(10,2) NOT NULL,
  `topartyname` varchar(200) DEFAULT NULL,
  `fromaccountid` INT NOT NULL,
  `fromemployeeid` varchar(25) NOT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `includeinreport` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  `orderamount` decimal(10,2) DEFAULT '0.00',
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `igst` decimal(10,2) DEFAULT '0.00',
  `extra` decimal(10,2) DEFAULT '0.00',
  `topartygstno` varchar(20) DEFAULT NULL,
  `topartymobileno` varchar(15) DEFAULT NULL,
  `paid` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `internaltransfer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT DEFAULT NULL,
  `toaccountid` INT DEFAULT NULL,
  `rcvddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `toemployeeid` varchar(25) DEFAULT NULL,
  `fromemployeeid` varchar(25) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `internaltransfer_ibfk_1` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `internaltransfer_ibfk_2` FOREIGN KEY (`toaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `internaltransfer_ibfk_3` FOREIGN KEY (`fromemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `internaltransfer_ibfk_4` FOREIGN KEY (`toemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `internaltransfer_ibfk_5` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `internaltransfer_ibfk_6` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `internaltransfer_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT DEFAULT NULL,
  `toaccountid` INT DEFAULT NULL,
  `rcvddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `toemployeeid` varchar(25) DEFAULT NULL,
  `fromemployeeid` varchar(25) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `noofemployee` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `org_subscription` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orgid` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_subscription_ibfk1_idx` (`orgid`),
  CONSTRAINT `org_subscription_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `orgdetails` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `mobile1` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile2` varchar(15) DEFAULT NULL,
  `gstnumber` varchar(20) DEFAULT NULL,
  `pannumber` varchar(15) DEFAULT NULL,
  `companytype` INT DEFAULT NULL,
  `businessnature` INT DEFAULT NULL,
  `numberofemployees` INT DEFAULT NULL,
  `extradetails` varchar(500) DEFAULT NULL,
  `accountdetails` varchar(500) DEFAULT NULL,
  `billingaddress` varchar(800) DEFAULT NULL,
  `caname` varchar(80) DEFAULT NULL,
  `caemail` varchar(255) DEFAULT NULL,
  `camobile` varchar(15) DEFAULT NULL,
  `orgid` INT DEFAULT NULL,
  `autoemailgstreport` tinyint(1) DEFAULT NULL,
  `website` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `orgid_UNIQUE` (`orgid`),
  KEY `orgdetails_ibfk_1_idx` (`orgid`),
  CONSTRAINT `orgdetails_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `otphandler` (
  `mobileNumber` varchar(15) NOT NULL,
  `otp` varchar(8) NOT NULL,
  `generatedtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`mobileNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `p_gst_reports` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orgid` INT NOT NULL,
  `pdffilepath` varchar(256) DEFAULT NULL,
  `excelfilepath` varchar(256) DEFAULT NULL,
  `generatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `generatedby` varchar(150) DEFAULT NULL,
  `fromEmail` varchar(100) DEFAULT NULL,
  `mode` varchar(20) DEFAULT NULL,
  `reportstatus` varchar(20) DEFAULT NULL,
  `formonth` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_gst_reports_ibfk_1` (`orgid`),
  CONSTRAINT `p_gst_reports_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table will store the gst related process data for all the organization';

CREATE TABLE `p_gstrpt_send_email` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `p_gst_reports_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_gstrpt_send_email_fk` (`p_gst_reports_id`),
  CONSTRAINT `p_gstrpt_send_email_fk` FOREIGN KEY (`p_gst_reports_id`) REFERENCES `p_gst_reports` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This is a process table which will maintain a email sent of a report.';

CREATE TABLE `p_txn_reports` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orgid` INT NOT NULL,
  `pdffilepath` varchar(256) DEFAULT NULL,
  `excelfilepath` varchar(256) DEFAULT NULL,
  `generatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `generatedby` varchar(150) DEFAULT NULL,
  `reportstatus` varchar(20) DEFAULT NULL,
  `period` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `format` varchar(20) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_txn_reports_ibfk_1` (`orgid`),
  CONSTRAINT `p_txn_reports_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table will store the transaction related process data for all the organization';

CREATE TABLE `p_txnrpt_send_email` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `p_txn_reports_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_txnrpt_send_email_fk` (`p_txn_reports_id`),
  CONSTRAINT `p_txnrpt_send_email_fk` FOREIGN KEY (`p_txn_reports_id`) REFERENCES `p_txn_reports` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This is a process table which will maintain a email sent of Transaction report.';

CREATE TABLE `paymentreceived` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT NOT NULL,
  `toaccountid` INT NOT NULL,
  `paymenttype` varchar(20) DEFAULT NULL,
  `rcvddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` DECIMAL(10,2) NOT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `fromcustomerid` varchar(25) NOT NULL,
  `toemployeeid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `details` varchar(200) DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `fromcustomerid` (`fromcustomerid`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `paymentreceived_ibfk_1` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `paymentreceived_ibfk_2` FOREIGN KEY (`toaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `paymentreceived_ibfk_3` FOREIGN KEY (`fromcustomerid`) REFERENCES `customer` (`id`),
  CONSTRAINT `paymentreceived_ibfk_4` FOREIGN KEY (`toemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `paymentreceived_ibfk_5` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `paymentreceived_ibfk_6` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `paymentreceived_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT NOT NULL,
  `toaccountid` INT NOT NULL,
  `paymenttype` varchar(20) DEFAULT NULL,
  `rcvddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `fromcustomerid` varchar(25) NOT NULL,
  `toemployeeid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `details` varchar(200) DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `fromcustomerid` (`fromcustomerid`),
  KEY `toemployeeid` (`toemployeeid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `paytovendor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT DEFAULT NULL,
  `toaccountid` INT DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `fromemployeeid` varchar(25) DEFAULT NULL,
  `tocustomerid` varchar(25) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `tocustomerid` (`tocustomerid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `paytovendor_ibfk_1` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `paytovendor_ibfk_2` FOREIGN KEY (`toaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `paytovendor_ibfk_3` FOREIGN KEY (`fromemployeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `paytovendor_ibfk_4` FOREIGN KEY (`tocustomerid`) REFERENCES `customer` (`id`),
  CONSTRAINT `paytovendor_ibfk_5` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `paytovendor_ibfk_6` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `paytovendor_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromaccountid` INT DEFAULT NULL,
  `toaccountid` INT DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `details` varchar(100) DEFAULT NULL,
  `fromemployeeid` varchar(25) DEFAULT NULL,
  `tocustomerid` varchar(25) DEFAULT NULL,
  `orgid` INT NOT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `toaccountid` (`toaccountid`),
  KEY `fromemployeeid` (`fromemployeeid`),
  KEY `tocustomerid` (`tocustomerid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `phonebook` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orgid` INT DEFAULT NULL,
  `employeeid` varchar(25) DEFAULT NULL,
  `sync_status` varchar(15) DEFAULT NULL,
  `lastsyncdate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phonebook_ibfk_2` (`orgid`),
  KEY `phonebook_ibfk_3` (`employeeid`),
  CONSTRAINT `phonebook_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `phonebook_ibfk_3` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table is master for storing all the contacts coming from UI. This stores information like for which employee this address book is.';

CREATE TABLE `phonebook_contact` (
  `contact_name` varchar(15) DEFAULT NULL,
  `key` varchar(15) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `isGlobal` tinyint(4) DEFAULT NULL,
  `phonebookid` INT DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  KEY `phonebook_contact_ibfk_1` (`phonebookid`),
  CONSTRAINT `phonebook_contact_ibfk_1` FOREIGN KEY (`phonebookid`) REFERENCES `phonebook` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Self- Idenpedent table for storing a contact. Key would be like mobile, website, or email and ''value'' will be value of the key. IsGlobal is mainly to designated whether the contact is under global category meaning can be shown to all. ''contact_name'' is name for that contact. ';

CREATE TABLE `purchasefromvendor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromcustomerid` varchar(25) DEFAULT NULL,
  `date` timestamp NOT NULL,
  `orderamount` decimal(10,2) NOT NULL,
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `igst` decimal(10,2) DEFAULT '0.00',
  `extra` decimal(10,2) DEFAULT '0.00',
  `totalamount` decimal(10,2) DEFAULT NULL,
  `note` varchar(256) DEFAULT NULL,
  `billno` varchar(100) DEFAULT NULL,
  `fromaccountid` INT NOT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `includeinreport` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromcustomerid` (`fromcustomerid`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `purchasefromvendor_ibfk_1` FOREIGN KEY (`fromcustomerid`) REFERENCES `customer` (`id`),
  CONSTRAINT `purchasefromvendor_ibfk_2` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `purchasefromvendor_ibfk_3` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `purchasefromvendor_ibfk_4` FOREIGN KEY (`createdbyid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `purchasefromvendor_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromcustomerid` varchar(25) DEFAULT NULL,
  `date` timestamp NOT NULL,
  `orderamount` decimal(10,2) NOT NULL,
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `igst` decimal(10,2) DEFAULT '0.00',
  `extra` decimal(10,2) DEFAULT '0.00',
  `totalamount` decimal(10,2) DEFAULT NULL,
  `note` varchar(256) DEFAULT NULL,
  `billno` varchar(100) DEFAULT NULL,
  `fromaccountid` INT NOT NULL,
  `orgid` INT NOT NULL,
  `includeincalc` tinyint(1) DEFAULT NULL,
  `includeinreport` tinyint(1) DEFAULT NULL,
  `createdbyid` varchar(25) NOT NULL,
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromcustomerid` (`fromcustomerid`),
  KEY `fromaccountid` (`fromaccountid`),
  KEY `orgid` (`orgid`),
  KEY `createdbyid` (`createdbyid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `retailsale` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `notes` varchar(150) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `fromemployeeid` varchar(25) NOT NULL,
  `fromaccountid` INT NOT NULL,
  `orgid` INT DEFAULT NULL,
  `createdbyid` varchar(25) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `includeincalc` tinyint(4) DEFAULT NULL,
  `createdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid_idx` (`fromaccountid`),
  KEY `fromemployeeid_idx` (`fromemployeeid`),
  CONSTRAINT `fromaccountid` FOREIGN KEY (`fromaccountid`) REFERENCES `account` (`id`),
  CONSTRAINT `fromemployeeid` FOREIGN KEY (`fromemployeeid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table will track the retail sale';

CREATE TABLE `retailsale_archive` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `notes` varchar(150) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `fromemployeeid` varchar(25) NOT NULL,
  `fromaccountid` INT NOT NULL,
  `orgid` INT DEFAULT NULL,
  `createdbyid` varchar(25) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `includeincalc` tinyint(4) DEFAULT NULL,
  `createdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fromaccountid_idx` (`fromaccountid`),
  KEY `fromemployeeid_idx` (`fromemployeeid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table will track the retail sale';

CREATE TABLE `role_permission` (
  `empTypeId` INT NOT NULL,
  `permissionId` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `systemconfigurations` (
  `key` varchar(75) DEFAULT 'null',
  `value` varchar(2000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;






DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `ArchiveAndPurgeProcess_Procedure`(
IN NoOfDaysAfterDeletion INT,
IN PurgeArchiveTableName VARCHAR(100)
)
BEGIN
	SET @sql = CONCAT('insert into ',PurgeArchiveTableName,'_archive  (select * from ',PurgeArchiveTableName,' where isdeleted = true and deletedate < date(now()) + interval -', NoOfDaysAfterDeletion ,' day) ;');
    PREPARE stmt1 from @sql;
    EXECUTE stmt1 ;

    set @sql2 = CONCAt ('delete from ', PurgeArchiveTableName, ' where isdeleted = true and deletedate < date(now()) + interval -', NoOfDaysAfterDeletion ,' day ;');
	PREPARE stmt2 from @sql2;
    EXECUTE stmt2 ;
END$$
DELIMITER ;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `ControlOrgDataGrowthDateWise_Procedure`(
IN TableName Varchar(75),
IN RegularDaysAfterDelete Int,
IN PremiumDaysAfterDelete Int
)
BEGIN

SET @sql = CONCAT('update ',TableName,' set `isdeleted` = true, `deletedate` = curdate() where `orgid` not in (select `orgid` from `org_subscription`) and
date_sub(curdate(), Interval ',RegularDaysAfterDelete,' day) > `createdate` or (orgid in(select `orgid` from `org_subscription`) and
date_sub(curdate(), Interval ',PremiumDaysAfterDelete, ' day)> `createdate`);');

PREPARE stmt from @sql;
EXECUTE stmt ;

END$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `ControlOrgDataGrowthTransactionWise_Procedure`(
IN RegularNoOfTxn Int,
IN PremiumNoOFTxn Int,
IN TableName Varchar(75)
)
BEGIN
declare iterator int;
declare countTempRows int;

SET @sql2 = CONCAT('CREATE TEMPORARY TABLE IF NOT EXISTS tempotable AS select count(id) , orgid mainorgid , (select id from org_subscription t where t.orgid=mainOrgID)
 from ', TableName ,' group by orgid having (count(id)> ',RegularNoOfTxn,' and orgid NOT IN (select orgid from org_subscription) )
 or (count(id)>',PremiumNoOFTxn,' and orgid IN(select orgid from org_subscription) ) ;');
PREPARE stmt from @sql2;
EXECUTE stmt;
select count(*) from tempotable into countTempRows;
set iterator = 0;
while(iterator<countTempRows) do
	set @sql3 = CONCAT('Select * into @count,@ParamOrgId,@isPremium from tempotable limit 1 offset ',iterator,';');
	PREPARE stmt from @sql3;
	EXECUTE stmt;
	call InternalDataControl_Procedure (TableName,RegularNoOfTxn,PremiumNoOFTxn,@ParamOrgId,@count,@isPremium);
  set iterator = iterator +1;
end while;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `EmployeeSalaryDueTotal_Procedure`(
	IN ParamOrgId INT,
    OUT totalSalaryDue DECIMAL(10,2)
	)
BEGIN
SELECT IF(sum(CurrentSalaryBalance) IS NULL, 0 , sum(CurrentSalaryBalance) ) INTO totalSalaryDue FROM employee WHERE CurrentSalaryBalance>0 and OrgId= ParamOrgId ;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `CustomerVendorBalanceTracking_Procedure`(
	IN ParamOrgId INT,
    OUT positiveBalance DECIMAL(10,2),
	OUT negativeBalance DECIMAL(10,2),
	OUT totalBalance DECIMAL(10,2))
BEGIN
 SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO positiveBalance FROM account 	WHERE CurrentBalance >0 and OrgId= ParamOrgId and type='Customer';
 SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO negativeBalance FROM account 	WHERE CurrentBalance <0 and OrgId=ParamOrgId and type='Customer';
 SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO totalBalance FROM account WHERE  OrgId=ParamOrgId and type='Customer';
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `InternalDataControl_Procedure`(
IN TableName Varchar(75),
IN RegularNoOfTxn Int,
IN PremiumNoOFTxn Int,
IN ParamOrgId long,
IN count Int,
In isPremium long
)
BEGIN
if(isPremium is  null) then
	if(count > RegularNoOfTxn) then
		SET @sql = CONCAT('update ',TableName,' set `isdeleted` = true, `deletedate` = curdate()
		where orgid = ', ParamOrgId, ' order by createdate LIMIT ',(count - RegularNoOfTxn),';');
   end if;
else
	if(count > PremiumNoOFTxn) then
		SET @sql = CONCAT('update ',TableName,' set `isdeleted` = true, `deletedate` = curdate()
		where orgid = ', ParamOrgId, ' order by createdate LIMIT ',(count - PremiumNoOFTxn),';');
    end if;
End if;
PREPARE stmt1 from @sql;
EXECUTE stmt1 ;
END$$
DELIMITER ;



DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `SoftDeleteAuditData_Procedure`(
	IN ParamOrgId INT,
    IN RecordId LONG,
	IN Identifier varchar(15),
    IN DeleteAll boolean
)
BEGIN
	if(Identifier = 'EMPLOYEE') then
		If(DeleteAll = true and RecordId <=0) then
			update employeeaudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId;
		else if(DeleteAll = false and RecordId >0) then
			update employeeaudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId and `id` = RecordId;
        End if;
        End if;
	else
		If(deleteAll = true and RecordId <=0) then
			update customeraudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId;
		else if(DeleteAll = false and RecordId >0) then
			update customeraudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId and `id` = RecordId;
        End if;
		End if;
    End if;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `SoftDeleteOrgData_Procedure`(
	IN RetainGSTTransaction boolean,
	IN RetainStaffSalaryTransaction boolean,
	IN RetainStaffAccountBalance boolean,
    IN RetainCustomerBalance boolean,
    IN RetainStaffSalaryBalance boolean,

    IN ParamOrgId LONG
)
BEGIN
	update delivery SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update internaltransfer SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update paymentreceived SET `isdeleted` = true , `deletedate`= CURDATE() where orgid =ParamOrgId;
	update paytovendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update retailsale SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;

if(RetainGSTTransaction = true) then
	update `generalexpense` SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId and includeinreport = false and includeincalc = false;
	update `customerinvoice` SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId and includeinreport = false and includeincalc = false;
	update `purchasefromvendor` SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId and includeinreport = false and includeincalc = false;
 END if;
IF(RetainStaffSalaryTransaction = true) then
	update employeesalarypayment SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update employeesalary SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;
If(RetainStaffSalaryBalance = true) then
   update employee  SET `lastsalarybalance` = `currentsalarybalance`, `currentsalarybalance` = 0  where orgid = ParamOrgId;
END if;
If(RetainStaffAccountBalance = true) then
	update `account` SET `lastbalance` = `currentbalance`, `currentbalance` = 0 where type = 'EMPLOYEE' and orgid = ParamOrgId;
END if;
If(RetainCustomerBalance = true) then
	update `account` SET `lastbalance` = `currentbalance`, `currentbalance` = 0 where type = 'CUSTOMER' and orgid = ParamOrgId;
END if;
End$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `Restore_SoftDeletedOrgData`(
	IN ParamOrgId INT
  )
BEGIN
    SET SQL_SAFE_UPDATES = 0;
	-- Restoring Main data which is in the softDeleted state
	update customerinvoice SET `isdeleted` = false where orgid = ParamOrgId and `isdeleted`=true;
	update delivery SET `isdeleted` = false where orgid = ParamOrgId and `isdeleted`=true;
	update generalexpense SET `isdeleted` = false where orgid = ParamOrgId and `isdeleted`=true;
	update internaltransfer SET `isdeleted` = false where orgid = ParamOrgId and `isdeleted`=true;
	update paymentreceived SET `isdeleted` = false  where orgid =ParamOrgId and `isdeleted`=true;
	update paytovendor SET `isdeleted` = false  where orgid = ParamOrgId and `isdeleted`=true;
	update purchasefromvendor SET `isdeleted` = false  where orgid = ParamOrgId and `isdeleted`=true;
	update retailsale SET `isdeleted` = false where orgid = ParamOrgId and `isdeleted`=true;
	update employeesalarypayment SET `isdeleted` = false  where orgid = ParamOrgId and `isdeleted`=true;
	update employeesalary SET `isdeleted` = false  where orgid = ParamOrgId and `isdeleted`=true;
	update employeeaudit SET `isdeleted` = false  where orgid = ParamOrgId and `isdeleted`=true;
	update customeraudit SET `isdeleted` = false  where orgid = ParamOrgId and `isdeleted`=true;

	-- Restoring balance from lastBalance (the organization should not have done any transaction)
	update calculationtracker  SET `unpaidexpense` = `lastunpaidexpense`, `paidexpense` = `lastpaidexpense` where orgid = ParamOrgId;

    update employee  SET `currentsalarybalance` =`lastsalarybalance`  where orgid = ParamOrgId;
	update `account` SET `currentbalance` = `lastbalance` where type = 'Employee' and orgid = ParamOrgId;
	update `account` SET `currentbalance` = `lastbalance` where type = 'Customer' and orgid = ParamOrgId;

End$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `TotalCashInHand_Procedure`(
	IN ParamOrgId INT,
    OUT totalCashInHand DECIMAL(10,2)
	)
BEGIN
SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO totalCashInHand FROM account WHERE OrgId= ParamOrgId and type='Employee' ;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `UpdateCustomerBalanceWithAudit_Procedure`(
	IN customerid varchar(25),
    IN accountid bigint,
	IN amount DECIMAL(10,2),
	IN otherparty  varchar(80),
    IN txntype varchar(25),
    IN operation varchar(15),
    IN orgid bigint,
    IN txndate date
)
BEGIN
DECLARE newlastBalance INT DEFAULT 0;
DECLARE newcurrentBalance INT DEFAULT 0;
update account set lastbalance = currentbalance  where id = accountid;
IF operation = 'ADD' THEN
update account set currentbalance = (currentbalance + amount) where id = accountid;
ELSE
update account set currentbalance = (currentbalance - amount) where id = accountid;
END IF;
select lastbalance,currentbalance INTO newlastBalance,newcurrentBalance from account where id=accountid;
insert into customeraudit(`customerid`,`accountid`,`amount`,`operation`,`otherparty`,`newbalance`,`previousbalance`,`type`,`orgid`,`txndate`)
 values (customerid,accountid,amount,operation, otherparty,newcurrentBalance,newlastbalance,txntype,orgid,txndate);
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `UpdateEmployeeBalanceWithAudit_Procedure`(
	IN employeeid varchar(25),
    IN accountid bigint,
	IN amount DECIMAL(10,2),
	IN otherparty  varchar(80),
    IN txntype varchar(25),
    IN operation varchar(15),
    IN forwhat varchar(30),
    IN orgid bigint,
    IN txndate date
)
BEGIN
DECLARE newlastBalance INT DEFAULT 0;
DECLARE newcurrentBalance INT DEFAULT 0;
update account set lastbalance = currentbalance  where id = accountid;
IF operation = 'ADD' THEN
update account set currentbalance = (currentbalance + amount) where id = accountid;
ELSE
update account set currentbalance = (currentbalance - amount) where id = accountid;
END IF;
select lastbalance,currentbalance INTO newlastBalance,newcurrentBalance from account where id=accountid;
insert into employeeaudit(`employeeid`,`accountid`,`amount`,`operation`,`otherparty`,`newbalance`,`previousbalance`,`type`,`forwhat`,`orgid`,`txndate`)
 values (employeeid,accountid,amount,operation, otherparty,newcurrentBalance,newlastbalance,txntype,forwhat,orgid,txndate);
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `UpdateEmployeeSalaryBalanceWithAudit_Procedure`(
	IN employeeid varchar(25),
    IN orgid bigint,
	IN amount DECIMAL(10,2),
	IN otherparty  varchar(80),
    IN txntype varchar(25),
    IN operation varchar(15),
    IN txndate date
)
BEGIN
DECLARE newlastBalance INT DEFAULT 0;
DECLARE newcurrentBalance INT DEFAULT 0;
/* Backing up the balances */
update employee set lastsalarybalance = currentsalarybalance  where id = employeeid;

/* adjusting the balances on the employee table */
IF operation = 'ADD' THEN
update employee set currentsalarybalance = (currentsalarybalance + amount) where id =  employeeid;
ELSE
update employee set currentsalarybalance = (currentsalarybalance - amount) where id = employeeid;
END IF;


select lastsalarybalance,currentsalarybalance INTO newlastBalance,newcurrentBalance from employee where id=employeeid;
insert into employee_salary_audit(`employeeid`,`amount`,`operation`,`otherparty`,`newbalance`,`previousbalance`,`type`,`orgid`,`txndate`)
 values (employeeid,amount,operation, otherparty,newcurrentBalance,newlastbalance,txntype,orgid,txndate);
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` FUNCTION `getcustomerId`() RETURNS varchar(15) CHARSET utf8mb4
    READS SQL DATA
BEGIN
declare customerId varchar(10);
INSERT INTO customer_seq VALUES (NULL);
SET customerId=CONCAT('C', LAST_INSERT_ID());
RETURN customerId;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` FUNCTION `getEmployeeId`() RETURNS varchar(15) CHARSET utf8mb4
    READS SQL DATA
BEGIN
declare employeeId varchar(10);
INSERT INTO employee_seq VALUES (NULL);
SET employeeId=CONCAT('E', LAST_INSERT_ID());
RETURN employeeId;
END$$
DELIMITER ;


INSERT INTO `expense_cat` (`id`, `name`) VALUES (1,'Bank Loan'),(2,'Transporting'),(3,'Weight Lifting'),(4,'Miscellaneous'),(5,'Tea Coffee - Refreshment'),(6,'Marketing'),(7,'Electricity Bill'),(8,'Other Bill'),(9,'Other'),(10,'Repairing'),(11,'Daily Wages'),(12,'Small Purchase');
INSERT INTO `employeetype` (`id`, `name`) VALUES (5,'accountant'),(7,'default_employee'),(4,'labour'),(2,'partner'),(3,'sales'),(1,'SUPER_ADMIN'),(6,'VIRTUAL');
INSERT INTO `businessnature` (`id`, `name`) VALUES (1,'Manufacturer'),(2,'Service Provider'),(3,'Trader / Re-seller / Shop Keeper');
INSERT INTO `role_permission` (`empTypeId`, `permissionId`) VALUES (1,'1'),(2,'1'),(2,'2'),(2,'3'),(2,'4');
INSERT INTO `systemconfigurations` (`key`, `value`) VALUES ('ArchivePurge.archiveandpurgetablelist','retailsale,purchasefromvendor,paytovendor,paymentreceived,internaltransfer,generalexpense,employeesalarypayment,employeesalary,employeeaudit,delivery,customerinvoice,customeraudit'),('ArchivePurge.DeleteAfterNumberOfDays','15'),('EMAIL.UTILITY.HOST','smtp.gmail.com'),('EMAIL.UTILITY.PORT','587'),('EMAIL.UTILITY.PASSWORD','Qwerty1@3'),('EMAIL.UTILITY.FROMEMAILADDRESS','mtest6551@gmail.com'),('EMAIL.UTILITY.TRUST','smtp.gmail.com'),('EMAIL.UTILITY.SSLEnabled','true'),('CONTROL.ORG.DATA.GROWTH.AUTOMATICDELETEAFTERDAYS','180'),('CONTROL.ORG.DATA.GROWTH.AUTOMATICDELETEONNUMBEROFTRANSACTION','200'),('CONTROL.ORG.DATA.GROWTH.PREMIUMDELETEDAYS','500'),('CONTROL.ORG.DATA.GROWTH.PREMIUMCDELETEONNUMBEROFTRANSACTION','800'),('CONTROL.ORG.DATA.GROWTH.TABLENAME','retailsale,purchasefromvendor,paytovendor,paymentreceived,internaltransfer,generalexpense,employeesalarypayment,employeesalary,delivery,customerinvoice'),('REPORT.PATH','F:/'),('REPORT.MAX.REPORTS.FOR.ONE.MONTH','3');
INSERT INTO `companytype` (`id`, `name`) VALUES (1,'One Person Company'),(2,'Private Ltd Company'),(3,'Public Ltd Company'),(4,'Limited Liability Partnership (LLP) '),(5,'Proprietorship / HUF'),(6,'Partnership'),(7,'Cooperatives');




