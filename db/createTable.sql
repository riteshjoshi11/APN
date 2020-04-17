use antrackerdb ;
DROP TABLE IF EXISTS  customer,delivery,customer_billing,PaymentReceived,Account;

CREATE DATABASE antrackerdb ; 
use antrackerdb; 
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `city` varchar(200),
  `dob` timestamp NULL DEFAULT NULL,
  `orgId` int NOT NULL,
  `GSTIN` varchar(200),
  `Transporter` varchar(200),
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `delivery` (
  `date` timestamp Not NULL ,
  `customerId` int  NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL
) ;


CREATE TABLE `customer_billing` (
  `id` int NOT NULL AUTO_INCREMENT,
   `customerId` int,
  `date` timestamp Not NULL ,
  `item`varchar(200) DEFAULT NULL,
  `amount` float NOT NULL,
  `CGST` float default 0,
  `SGST` float default 0,
  `IGST` float default 0,
  `extra` float default 0,
 `IncludeInCalc`  Boolean,
  `total`  float,
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
  `note`varchar(256) DEFAULT NULL,
  `includeInReport` boolean,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;  


CREATE TABLE `Expense` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` timestamp not NULL DEFAULT NOW(),
  `Category` varchar(200) NOT NULL,
  `Description` varchar(200),
  `amount` float NOT NULL,
  `toPartyName` varchar(200),
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
  `FromAccountID` int NOT NULL ,
  `ToAccountID` varchar(200),
  `IncludeInCalc`  Boolean,	
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `expense_cat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
   PRIMARY KEY (`id`)	
) ENGINE=InnoDB AUTO_INCREMENT=1;


CREATE TABLE `PaymentReceived` (
  `id` int NOT NULL AUTO_INCREMENT,
  `FromAccountID` varchar(20) ,  
  `ToAccountID` varchar(20) , 
  `PaymentType` varchar(20) NOT NULL,
  `RcvdDate` timestamp not NULL DEFAULT NOW(),
  `amount` float NOT NULL,
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
  `IncludeInCalc`  Boolean,	
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `OwnerID` varchar(20) ,  
  `AccountNickName` varchar(20) , 
  `Type` varchar(20) NOT NULL,
  `Details` varchar(20),
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

