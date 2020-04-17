create database antrackerdb ; 
use antrackerdb; 
drop table if exists  organization,customer,delivery,customerinvoice,paymentreceived,account,purchasefromvendor,generalexpense,paytovendor,internaltransfer,employee,employeesalary,employeesalarypayment,city,expense_cat;

create table `organization` (
`id` int not null auto_increment,
`orgname`  varchar(200) not null,
`companyid` varchar(50) not null,
 primary key (`id`)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

create table `employee` (
  `id` int not null auto_increment,
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
  ) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `customer` (
  `id` int not null auto_increment,
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
   FOREIGN KEY (orgid) REFERENCES organization (id) 
   -- here we are not creating fk relationship for 'createdbyid' with employee
   -- as to ensure customer can be deleted without deleting employee
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `account` (
  `id` int not null auto_increment,
  `ownerid` int,  
  `accountnickname` varchar(300) , 
  `type` varchar(20) not null,
  `details` varchar(20),
  `orgid` int not null,
  `createdbyid` int  not null,
  `currentbalance` float  not null,
  `lastbalance` float  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (ownerid) REFERENCES customer (id),
   FOREIGN KEY (ownerid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `delivery` (
  `id` int not null auto_increment,
  `date` timestamp not null ,
  `tocustomerid` int  not null,
  `description` varchar(200) default null,
  `orgid` int not null,
  `createdbyid` int not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (tocustomerid) REFERENCES customer (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb auto_increment=3 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `customerinvoice` (
  `id` int not null auto_increment,
  `tocustomerid` int,
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
  `createdbyid` int  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (tocustomerid) REFERENCES customer (id) ,
   FOREIGN KEY (toaccountid) REFERENCES account (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;  

create table `purchasefromvendor` (
  `id` int not null auto_increment,
  `fromcustomerid` int,
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
  `createdbyid` int  not null,
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
  `fromemployeeid` int not null,
  `orgid` int not null,
  `includeincalc`  boolean,	
  `includeinreport` boolean,
  `createdbyid` int  not null,
  `createdate` timestamp default current_timestamp,
   primary key (`id`),
   FOREIGN KEY (fromaccountid) REFERENCES account (id),
   FOREIGN KEY (fromemployeeid) REFERENCES employee (id),
   FOREIGN KEY (orgid) REFERENCES organization (id),
   FOREIGN KEY (createdbyid) REFERENCES employee (id)
) engine=innodb  default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;


create table `paymentreceived` (
  `id` int not null auto_increment,
  `fromaccountid` int ,  
  `toaccountid` int , 
  `paymenttype` varchar(20) not null,
  `rcvddate` timestamp not null default now(),
  `amount` float not null,
  `orgid` int not null,
  `createdbyid` int  not null,
  `includeincalc`  boolean,	
  `fromcustomerid` int ,  
  `toemployeeid` int, 
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
  `fromemployeeid` int , 
  `tocustomerid` int ,  
  `orgid` int not null,
  `createdbyid` int  not null,
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
  `toemployeeid` int ,  
  `fromemployeeid` int , 
  `orgid` int not null,
  `createdbyid` int not null,
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
  `toemployeeid` int not null,
  `amount` float not null,
  `details` varchar(100) , 
  `orgid` int not null,
  `includeincalc`  boolean,	
  `createdbyid` int not null,
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
  `toemployeeid` int not null ,  
  `fromemployeeid` int not null , 
  `orgid` int not null,
  `includeincalc`  boolean,	
  `createdbyid` int  not null,
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
