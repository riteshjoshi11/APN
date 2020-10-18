ALTER TABLE `antrackerdb`.`account`
CHANGE COLUMN `currentbalance` `currentbalance` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `lastbalance` `lastbalance` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `initialbalance` `initialbalance` DECIMAL(10,2) NULL DEFAULT '0' ;

ALTER TABLE `antrackerdb`.`calculationtracker`
CHANGE COLUMN `unpaidexpense` `unpaidexpense` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `paidexpense` `paidexpense` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `totalexpense` `totalexpense` DECIMAL(10,2) GENERATED ALWAYS AS ((`unpaidexpense` + `paidexpense`)) VIRTUAL ,
CHANGE COLUMN `totalpaidsalary` `totalpaidsalary` DECIMAL(10,2) NULL DEFAULT '0' ;


ALTER TABLE `antrackerdb`.`customeraudit`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `newbalance` `newbalance` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `previousbalance` `previousbalance` DECIMAL(10,2) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`customeraudit_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `newbalance` `newbalance` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `previousbalance` `previousbalance` DECIMAL(10,2) NULL DEFAULT NULL ;

ALTER TABLE `antrackerdb`.`customerinvoice`
CHANGE COLUMN `orderamount` `orderamount` DECIMAL(10,2) NOT NULL ,
CHANGE COLUMN `cgst` `cgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `sgst` `sgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `igst` `igst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `extra` `extra` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `totalamount` `totalamount` DECIMAL(10,2) NULL DEFAULT '0' ;

ALTER TABLE `antrackerdb`.`customerinvoice_archive`
CHANGE COLUMN `orderamount` `orderamount` DECIMAL(10,2) NOT NULL ,
CHANGE COLUMN `cgst` `cgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `sgst` `sgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `igst` `igst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `extra` `extra` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `totalamount` `totalamount` DECIMAL(10,2) NULL DEFAULT '0' ;


ALTER TABLE `antrackerdb`.`employee`
CHANGE COLUMN `currentsalarybalance` `currentsalarybalance` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `lastsalarybalance` `lastsalarybalance` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `initialsalarybalance` `initialsalarybalance` DECIMAL(10,2) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`employee_salary_audit`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `otherparty` `otherparty` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `newbalance` `newbalance` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `previousbalance` `previousbalance` DECIMAL(10,2) NULL DEFAULT NULL ;





ALTER TABLE `antrackerdb`.`employeeaudit`
CHANGE COLUMN `newbalance` `newbalance` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `previousbalance` `previousbalance` DECIMAL(10,2) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`employeeaudit_archive`
CHANGE COLUMN `newbalance` `newbalance` DECIMAL(10,2) NULL DEFAULT NULL ,
CHANGE COLUMN `previousbalance` `previousbalance` DECIMAL(10,2) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`employeesalary`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;

ALTER TABLE `antrackerdb`.`employeesalary_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;


ALTER TABLE `antrackerdb`.`employeesalarypayment`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;


ALTER TABLE `antrackerdb`.`employeesalarypayment_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;


ALTER TABLE `antrackerdb`.`generalexpense`
CHANGE COLUMN `totalamount` `totalamount` DECIMAL(10,2) NOT NULL ,
CHANGE COLUMN `orderamount` `orderamount` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `cgst` `cgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `sgst` `sgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `igst` `igst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `extra` `extra` DECIMAL(10,2) NULL DEFAULT '0' ;


ALTER TABLE `antrackerdb`.`generalexpense_archive`
CHANGE COLUMN `totalamount` `totalamount` DECIMAL(10,2) NOT NULL ,
CHANGE COLUMN `orderamount` `orderamount` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `cgst` `cgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `sgst` `sgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `igst` `igst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `extra` `extra` DECIMAL(10,2) NULL DEFAULT '0' ;


ALTER TABLE `antrackerdb`.`internaltransfer`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;

ALTER TABLE `antrackerdb`.`internaltransfer_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;

ALTER TABLE `antrackerdb`.`paymentreceived`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;


ALTER TABLE `antrackerdb`.`paymentreceived_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;

ALTER TABLE `antrackerdb`.`paytovendor`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;


ALTER TABLE `antrackerdb`.`paytovendor_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;

ALTER TABLE `antrackerdb`.`purchasefromvendor`
CHANGE COLUMN `orderamount` `orderamount` DECIMAL(10,2) NOT NULL ,
CHANGE COLUMN `cgst` `cgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `sgst` `sgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `igst` `igst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `extra` `extra` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `totalamount` `totalamount` DECIMAL(10,2) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`purchasefromvendor_archive`
CHANGE COLUMN `orderamount` `orderamount` DECIMAL(10,2) NOT NULL ,
CHANGE COLUMN `cgst` `cgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `sgst` `sgst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `igst` `igst` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `extra` `extra` DECIMAL(10,2) NULL DEFAULT '0' ,
CHANGE COLUMN `totalamount` `totalamount` DECIMAL(10,2) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`retailsale`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;


ALTER TABLE `antrackerdb`.`retailsale_archive`
CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;

USE `antrackerdb`;
DROP procedure IF EXISTS `UpdateEmployeeSalaryBalanceWithAudit_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
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
update employee set currentsalarybalance = (currentsalarybalance + amount) where id = employeeid;
ELSE
update employee set currentsalarybalance = (currentsalarybalance - amount) where id = employeeid;
END IF;


select lastsalarybalance,currentsalarybalance INTO newlastBalance,newcurrentBalance from employee where id=employeeid;
insert into employee_salary_audit(`employeeid`,`amount`,`operation`,`otherparty`,`newbalance`,`previousbalance`,`type`,`orgid`,`txndate`)
 values (employeeid,amount,operation, otherparty,newcurrentBalance,newlastbalance,txntype,orgid,txndate);
END$$

DELIMITER ;


USE `antrackerdb`;
DROP procedure IF EXISTS `UpdateEmployeeBalanceWithAudit_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
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


USE `antrackerdb`;
DROP procedure IF EXISTS `UpdateCustomerBalanceWithAudit_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
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

USE `antrackerdb`;
DROP procedure IF EXISTS `TotalCashInHand_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `TotalCashInHand_Procedure`(
	IN ParamOrgId INT,
    OUT totalCashInHand DECIMAL(10,2)
	)
BEGIN
SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO totalCashInHand FROM account WHERE OrgId= ParamOrgId and type='Employee' ;
END$$

DELIMITER ;

USE `antrackerdb`;
DROP procedure IF EXISTS `EmployeeSalaryDueTotal_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `EmployeeSalaryDueTotal_Procedure`(
	IN ParamOrgId INT,
    OUT totalSalaryDue DECIMAL(10,2)
	)
BEGIN
SELECT IF(sum(CurrentSalaryBalance) IS NULL, 0 , sum(CurrentSalaryBalance) ) INTO totalSalaryDue FROM employee WHERE CurrentSalaryBalance>0 and OrgId= ParamOrgId ;
END$$

DELIMITER ;



USE `antrackerdb`;
DROP procedure IF EXISTS `CustomerVendorBalanceTracking_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
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





