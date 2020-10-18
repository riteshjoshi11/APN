 -- TESTED ON MYSQL 8.0.18
SHOW VARIABLES LIKE "version";
 
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'Sena_120';
GRANT ALL PRIVILEGES ON root.* TO 'root'@'%';

CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Sena_120';
GRANT ALL PRIVILEGES ON root.* TO 'root'@'localhost';
 

drop database antrackerdb;
create database antrackerdb ; 
use antrackerdb; 

drop table if exists  organization,customer,delivery,customerinvoice,paymentreceived,account,purchasefromvendor,generalexpense,paytovendor,internaltransfer,employee,employeesalary,employeesalarypayment,city,expense_cat,employee_seq,customer_seq;

create table `organization` (
`id` int not null auto_increment,
`orgname`  varchar(200) not null,
`companyid` varchar(50) not null,
 primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `employee` (
  `id` VARCHAR(25),
  `first` varchar(50) not null,
  `last` varchar(50) not null,
  `mobile` varchar(20),
  `loginrequired`  boolean,	
  `loginusername`  varchar(100),		
  `loginpassword`  varchar(25),	
  `type`  varchar(20),	
  `currentsalarybalance`  float,	
  `lastsalarybalance`  float,	
  `orgid` int not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (orgid) REFERENCES organization (id)
  ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `customer` (
  `id` VARCHAR(25),
  `name` varchar(200) not null,
  `city` varchar(200),
  `gstin` varchar(200),
  `transporter` varchar(200),
  `mobile1` varchar(20),
  `mobile2` varchar(20),
  `firmname` varchar(20),
  `billingadress` varchar(500),
  `orgid` int not null,
  `createdbyid` varchar(50)  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id) 
   -- here we are not creating fk relationship for 'createdbyid' with employee
   -- as to ensure customer can be deleted without deleting employee
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

create table `employee_seq` (
`id` int NOT NULL AUTO_INCREMENT,
primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

create table `customer_seq` (
`id` int NOT NULL AUTO_INCREMENT,
primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

delimiter //
CREATE TRIGGER `customer_BEFORE_INSERT` BEFORE INSERT ON `customer` FOR EACH ROW BEGIN
INSERT INTO customer_seq VALUES (NULL);
SET NEW.id = CONCAT('C', LAST_INSERT_ID());
END;//
delimiter ;

delimiter //
CREATE  TRIGGER `employee_BEFORE_INSERT` BEFORE INSERT ON `employee` FOR EACH ROW BEGIN
INSERT INTO employee_seq VALUES (NULL);
SET NEW.id = CONCAT('E', LAST_INSERT_ID());
END;//
delimiter ;

create table `account` (
  `id` int not null auto_increment,
  `ownerid` VARCHAR(25),  
  `accountnickname` varchar(300) , 
  `type` varchar(20) not null,
  `details` varchar(20),
  `orgid` int not null,
  `createdbyid` varchar(25)  not null,
  `currentbalance` float  not null,
  `lastbalance` float  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (orgid) REFERENCES organization (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `delivery` (
  `id` int not null auto_increment,
  `date` timestamp not null ,
  `tocustomerid` varchar(25)  not null,
  `description` varchar(200) default null,
  `orgid` int not null,
  `createdbyid` varchar(25) not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (tocustomerid) REFERENCES customer (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `customerinvoice` (
  `id` int not null auto_increment,
  `tocustomerid` varchar(25),
  `date` timestamp not null ,
  `item`varchar(200) default null,
  `amount` float not null,
  `cgst` float default 0,
  `sgst` float default 0,
  `igst` float default 0,
  `extra` float default 0,
  `total`  float default 0,
  `note`varchar(256) default null,
  `billno` varchar(100),
  `toaccountid` int,
  `orgid` int not null,
  `includeinreport` boolean,
  `includeincalc`  boolean,
  `createdbyid` varchar(25)  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (tocustomerid) REFERENCES customer (id) ,
   FOREIGN KEY (toaccountid) REFERENCES account (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;  

create table `purchasefromvendor` (
  `id` int not null auto_increment,
  `fromcustomerid`  varchar(25),
  `date` timestamp not null ,
  `amount` float not null,
  `cgst` float default 0,
  `sgst` float default 0,
  `igst` float default 0,
  `extra` float default 0,
  `total` float,
  `note`  varchar(256) default null,
  `billno` varchar(100),
  `fromaccountid` int not null,
  `orgid` int not null,
  `includeincalc`  boolean,
  `includeinreport` boolean,
  `createdbyid` varchar(25)  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromcustomerid) REFERENCES customer (id) ,
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;  



create table `generalexpense` (
  `id` int not null auto_increment,
  `date` timestamp not null default now(),
  `category` varchar(200) not null,
  `description` varchar(200),
  `amount` float not null,
  `topartyname` varchar(200),
  `fromaccountid` int not null ,
  `fromemployeeid` varchar(25) not null,
  `orgid` int not null,
  `includeincalc`  boolean,	
  `includeinreport` boolean,
  `createdbyid` varchar(25)  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (fromemployeeid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb  default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `paymentreceived` (
  `id` int not null auto_increment,
  `fromaccountid` int not null,  
  `toaccountid` int not null, 
  `paymenttype` varchar(20) not null,
  `rcvddate` timestamp not null default now(),
  `amount` float not null,
  `orgid` int not null,
  `createdbyid` varchar(25)  not null,
  `includeincalc`  boolean,	
  `fromcustomerid`  varchar(25) not null,  
  `toemployeeid` varchar(25) not null, 
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (toaccountid) REFERENCES account (id),
   FOREIGN KEY (fromcustomerid) REFERENCES customer (id),
   FOREIGN KEY (toemployeeid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `paytovendor` (
  `id` int not null auto_increment,
  `fromaccountid` int,  
  `toaccountid` int , 
  `rcvddate` timestamp not null default now(),
  `amount` float not null,
  `details` varchar(100) , 
  `fromemployeeid` varchar(25) , 
  `tocustomerid` varchar(25) ,  
  `orgid` int not null,
  `createdbyid` varchar(25)  not null,
  `includeincalc`  boolean,	
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (toaccountid) REFERENCES account (id),
   FOREIGN KEY (fromemployeeid) REFERENCES employee (id),
   FOREIGN KEY (tocustomerid) REFERENCES customer (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `internaltransfer` (
  `id` int not null auto_increment,
  `fromaccountid` int,  
  `toaccountid` int , 
  `rcvddate` timestamp not null default now(),
  `amount` float not null,
  `details` varchar(100) , 
  `toemployeeid` varchar(25) ,  
  `fromemployeeid` varchar(25) , 
  `orgid` int not null,
  `createdbyid` varchar(25) not null,
  `includeincalc`  boolean,	
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (toaccountid) REFERENCES account (id),
   FOREIGN KEY (fromemployeeid) REFERENCES employee (id),
   FOREIGN KEY (toemployeeid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;



create table `employeesalary` (
  `id` int not null auto_increment,
  `toemployeeid` varchar(25) not null,
  `amount` float not null,
  `details` varchar(100) , 
  `orgid` int not null,
  `includeincalc`  boolean,	
  `createdbyid` varchar(25) not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (toemployeeid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) ;

create table `employeesalarypayment` (
  `id` int not null auto_increment,
  `fromaccountid` int,  
  `transferdate` timestamp not null default now(),
  `amount` float not null,
  `details` varchar(100) , 
  `toemployeeid` varchar(25) not null ,  
  `fromemployeeid` varchar(25) not null , 
  `orgid` int not null,
  `includeincalc`  boolean,	
  `createdbyid` varchar(25)  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (fromemployeeid) REFERENCES employee (id),
   FOREIGN KEY (toemployeeid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `city` (
  `id` int not null auto_increment,
  `name` varchar(200) not null,
  primary key (`id`)
) engine=innodb auto_increment=1;

create table `expense_cat` (
  `id` int not null auto_increment,
  `name` varchar(200) not null,
   primary key (`id`)	
) engine=innodb auto_increment=1;

-- Data Insert Start
INSERT INTO `organization` VALUES (1,'AN Tracker','C001');


INSERT INTO `employee` VALUES ('E1','Nitesh','Yadav','9158990354',NULL,NULL,NULL,'',NULL,NULL,1,'2020-04-18 10:16:22'),('E2','Ritesh','Joshi','9158900654',NULL,NULL,NULL,NULL,NULL,NULL,1,'2020-04-18 10:16:40');

INSERT INTO `customer` VALUES ('C1','XYZ Corp','Pune',NULL,NULL,NULL,NULL,NULL,NULL,1,'E1','2020-04-18 10:17:08'),('C2','ABC Corp','Mhow',NULL,NULL,NULL,NULL,NULL,NULL,1,'E2','2020-04-18 10:17:45');


