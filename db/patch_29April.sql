-- DB Patch 19 April  
use antrackerdb;

INSERT INTO `antrackerdb`.`employee` (`first`, `last`, `mobile`, `orgid`) VALUES ('Avinash ', 'Yadav', '9158900054', '1');
INSERT INTO `antrackerdb`.`employee` (`first`, `last`, `mobile`, `orgid`) VALUES ('Ashok', 'Suradkar', '12121212', '1');
INSERT INTO `antrackerdb`.`account` (`ownerid`, `accountnickname`, `type`, `orgid`, `createdbyid`, `currentbalance`, `lastbalance`) VALUES ('E3', 'AviICICI', 'Employee', '1', 'E1', '5', '0');
INSERT INTO `antrackerdb`.`account` (`ownerid`, `accountnickname`, `type`, `orgid`, `createdbyid`, `currentbalance`, `lastbalance`) VALUES ('E3', 'AviSBI', 'Employee', '1', 'E1', '4', '0');
INSERT INTO `antrackerdb`.`customer` (`id`, `name`, `city`, `orgid`, `createdbyid`) VALUES ('', 'BCD Ltd', 'Indore', '1', 'E2');
INSERT INTO `antrackerdb`.`account` (`ownerid`, `accountnickname`, `type`, `orgid`, `createdbyid`, `currentbalance`, `lastbalance`) VALUES ('C1', 'Pune [XYZ Corp]', 'Customer', '1', 'E1', '4', '0');
INSERT INTO `antrackerdb`.`account` (`ownerid`, `accountnickname`, `type`, `orgid`, `createdbyid`, `currentbalance`, `lastbalance`) VALUES ('C2', 'Mhow [ABC Corp]', 'Customer', '1', 'E1', '3', '0');
INSERT INTO `antrackerdb`.`account` (`ownerid`, `accountnickname`, `type`, `orgid`, `createdbyid`, `currentbalance`, `lastbalance`) VALUES ('C3', 'Indore BCD Ltd ', 'Customer', '1', 'E1', '-3', '0');
UPDATE `antrackerdb`.`employee` SET `currentsalarybalance` = '50' WHERE (`id` = 'E1');
UPDATE `antrackerdb`.`employee` SET `currentsalarybalance` = '20' WHERE (`id` = 'E2');
UPDATE `antrackerdb`.`employee` SET `currentsalarybalance` = '10' WHERE (`id` = 'E3');
UPDATE `antrackerdb`.`employee` SET `currentsalarybalance` = '-1' WHERE (`id` = 'E4');
INSERT INTO `antrackerdb`.`organization` (`id`, `orgname`, `companyid`) VALUES ('2', 'madhuram', 'C002');
INSERT INTO `antrackerdb`.`employee` (`first`, `last`, `mobile`, `currentsalarybalance`, `orgid`) VALUES ('Sony', 'Bhai', '34234234', '10', '2');
INSERT INTO `antrackerdb`.`employee` (`first`, `last`, `mobile`, `currentsalarybalance`, `orgid`) VALUES ('raju', 'bhai', '', '-1', '2');
INSERT INTO `antrackerdb`.`employee` (`first`, `last`, `mobile`, `currentsalarybalance`, `orgid`) VALUES ('kaka', 'kaka', '', '2', '2');

create table `CalculationTracker` (
`orgid` int not null auto_increment,
`unpaidexpense` float default 0,
`paidexpense` float default 0,
`totalexpense` float as (`unpaidexpense` + `paidexpense`),
`totalpaidsalary` float default 0,
 FOREIGN KEY (orgid) REFERENCES organization (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

delimiter //
-- This procedure queries the account table for an organization
-- It takes the postive balance in the calculation i.e employee with some money in their hand. 
-- The negative balance is already covered as part of employee expenses, salary paid or payment made. 
CREATE  PROCEDURE `TotalCashInHand_Procedure`(
	IN ParamOrgId INT,
    OUT totalCashInHand float
	)
BEGIN
SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO totalCashInHand FROM account WHERE CurrentBalance>0 and OrgId= ParamOrgId and type='Employee' ;
END ;//
delimiter ;


call TotalCashInHand_Procedure(1,@totalcashinhand ) ;
select @totalcashinhand; 

delimiter //
-- This stored procedure tracks and gets following calculation parameters: 
-- NeedToPayCustomerVendor: Sum of all + balances (>0) from Account table of Type=Customer and Org ID=<Replace Org Id>
-- NeedToCollectCustomerVendor: (Sum of all -ve balances from Account table of Type=Customer and OrgId=<Replace Org ID>”)
-- TotalCustomerVendorBalance ( Sum (NeedToPay,NeedToCollect) )
CREATE PROCEDURE `CustomerVendorBalanceTracking_Procedure`(
	IN ParamOrgId INT,
    OUT positiveBalance float,
	OUT negativeBalance float,
	OUT totalBalance float)
BEGIN
 SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO positiveBalance FROM account 	WHERE CurrentBalance >0 and OrgId= ParamOrgId and type='Customer';
 SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO negativeBalance FROM account 	WHERE CurrentBalance <0 and OrgId=ParamOrgId and type='Customer';
 SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO totalBalance FROM account WHERE  OrgId=ParamOrgId and type='Customer';
END;//
delimiter ;

 call CustomerVendorBalanceTracking_Procedure(1,@NeedToPayCustomerVendor,@NeedToCollectCustomerVendor, @TotalCustomerVendorBalance ) ;
 select @NeedToPayCustomerVendor,@NeedToCollectCustomerVendor, @TotalCustomerVendorBalance ; 


delimiter //
-- This stored procedure gets the totalSalaryDue 
CREATE  PROCEDURE `EmployeeSalaryDueTotal_Procedure`(
	IN ParamOrgId INT,
    OUT totalSalaryDue float
	)
BEGIN
SELECT IF(sum(CurrentSalaryBalance) IS NULL, 0 , sum(CurrentSalaryBalance) ) INTO totalSalaryDue FROM employee WHERE CurrentSalaryBalance>0 and OrgId= ParamOrgId ;
END;//
delimiter ;

call EmployeeSalaryDueTotal_Procedure(2,@totalSalaryDue ) ;
select @totalSalaryDue; 

-- query employee with their balance
Select employee.id,employee.first,employee.last,employee.mobile, employee.loginrequired, employee.loginusername, 
employee.type,employee.currentsalarybalance, 
SUM(account.currentBalance) as empcashinhand from employee, account 
Where employee.id = Account.ownerid
and account.type='Employee'
and employee.orgid=1
group by Account.ownerid;


-- query customer with their balance
Select customer.id,customer.name,customer.city,customer.mobile1, customer.mobile2, customer.firmname, customer.transporter,
customer.billingadress,SUM(account.currentBalance) as customervendorbalance from customer, account 
where customer.id = Account.ownerid
and account.type='Customer'
and customer.orgid=1
group by Account.ownerid;

-- Delete password column
ALTER TABLE `antrackerdb`.`employee` 
DROP COLUMN `loginpassword`;


DROP TRIGGER IF EXISTS `antrackerdb`.`employee_BEFORE_UPDATE`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `antrackerdb`.`employee_BEFORE_UPDATE` BEFORE UPDATE ON `employee` FOR EACH ROW
BEGIN
IF NEW.currentsalarybalance <> OLD.currentsalarybalance THEN
 SET NEW.lastsalarybalance=OLD.currentsalarybalance ;
END IF;
END$$
DELIMITER ;


ALTER TABLE `antrackerdb`.`purchasefromvendor` 
CHANGE COLUMN `amount` `orderamount` FLOAT NOT NULL ,
CHANGE COLUMN `total` `totalamount` FLOAT NULL DEFAULT NULL ;

ALTER TABLE `antrackerdb`.`customerinvoice` 
CHANGE COLUMN `amount` `orderamount` FLOAT NOT NULL ,
CHANGE COLUMN `total` `totalamount` FLOAT NULL DEFAULT '0' ,
CHANGE COLUMN `billno` `invoiceno` VARCHAR(100) NULL DEFAULT NULL ;

ALTER TABLE `antrackerdb`.`customerinvoice` 
DROP COLUMN `item`;

ALTER TABLE `antrackerdb`.`generalexpense` 
ADD COLUMN `orderamount` FLOAT NULL DEFAULT 0 AFTER `createdate`,
ADD COLUMN `cgst` FLOAT NULL DEFAULT 0 AFTER `orderamount`,
ADD COLUMN `sgst` FLOAT NULL DEFAULT 0 AFTER `cgst`,
ADD COLUMN `igst` FLOAT NULL DEFAULT 0 AFTER `sgst`,
ADD COLUMN `extra` FLOAT NULL DEFAULT 0 AFTER `igst`,
CHANGE COLUMN `amount` `totalamount` FLOAT NOT NULL ;


ALTER TABLE `antrackerdb`.`paytovendor` 
CHANGE COLUMN `rcvddate` `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `antrackerdb`.`paymentreceived` 
ADD COLUMN `details` VARCHAR(200) NULL AFTER `createdate`;

drop table if exists  organization;

CREATE TABLE `organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgname` varchar(200) NOT NULL,
  `state` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

