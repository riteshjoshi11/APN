ALTER TABLE `antrackerdb`.`customeraudit` 
ADD COLUMN `orgid` BIGINT NOT NULL AFTER `date`;

ALTER TABLE `antrackerdb`.`employeeaudit` 
ADD COLUMN `orgid` BIGINT NOT NULL AFTER `date`;

ALTER TABLE `antrackerdb`.`employeeaudit` ADD COLUMN `txndate` datetime;
ALTER TABLE `antrackerdb`.`customeraudit` ADD COLUMN `txndate` datetime;


USE `antrackerdb`;
DROP procedure IF EXISTS `UpdateEmployeeBalanceWithAudit_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `UpdateEmployeeBalanceWithAudit_Procedure`(
	IN employeeid varchar(25),
    IN accountid bigint,
	IN amount double,
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
	IN amount double,
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

