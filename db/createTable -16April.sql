use antrackerdb ;
DROP TABLE IF EXISTS  organization,customer,delivery,customerInvoice,PaymentReceived,Account;

CREATE DATABASE antrackerdb ; 
use antrackerdb; 

CREATE TABLE `organization` (
`orgid` int NOT NULL AUTO_INCREMENT,
`orgName`  varchar(200) NOT NULL,
`companyid` varchar(50) NOT NULL,
 PRIMARY KEY (`orgid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `city` varchar(200),
  `orgId` int NOT NULL,
  `GSTIN` varchar(200),
  `Transporter` varchar(200),
  `mobile1` varchar(20),
  `mobile2` varchar(20),
  `firmName` varchar(20),
  `billingAdress` varchar(500),
  `createdById` varchar(50)  NOT NULL,
  `createDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `delivery` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` timestamp Not NULL ,
  `customerId` int  NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `orgId` int NOT NULL,
  `createdById` varchar(50)  NOT NULL,
  `createDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ;


CREATE TABLE `customerInvoice` (
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
  `billNo` varchar(100),
  `ToAccountId` varchar(100),
  `createDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;  

CREATE TABLE `purchasefromVendor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fromCustomerId` int,
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
  `billNo` varchar(100),
  `FromAccountId` varchar(100),
  `createDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;  



CREATE TABLE `GeneralExpense` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` timestamp not NULL DEFAULT NOW(),
  `Category` varchar(200) NOT NULL,
  `Description` varchar(200),
  `amount` float NOT NULL,
  `toPartyName` varchar(200),
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
  `FromAccountID` int NOT NULL ,
  `FromEmployeeID` varchar(200),
  `IncludeInCalc`  Boolean,	
  `createDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `PaymentReceived` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fromAccountID` varchar(20) ,  
  `toAccountID` varchar(20) , 
  `paymentType` varchar(20) NOT NULL,
  `rcvdDate` timestamp not NULL DEFAULT NOW(),
  `amount` float NOT NULL,
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
  `includeInCalc`  Boolean,	
  `fromCustomerID` varchar(20) ,  
  `toEmployeeID` varchar(20) , 
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ownerID` varchar(20) ,  
  `accountNickName` varchar(300) , 
  `type` varchar(20) NOT NULL,
  `details` varchar(20),
  `orgId` int NOT NULL,
  `createdById` int  NOT NULL,
  `currentBalance` float  NOT NULL,
  `lastBalance` float  NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


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
