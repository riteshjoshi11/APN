USE `antrackerdb`;

drop table if exists `customeraudit`  ; 

CREATE TABLE `customeraudit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customerid` varchar(45) DEFAULT NULL,
  `accountid` int(11) DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `forwhat` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` float DEFAULT NULL,
  `previousbalance` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`customerid`) REFERENCES customer (id),
  FOREIGN KEY (`accountid`) REFERENCES account (id)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the custome balance update audit function';


drop table if exists `employeeaudit`  ; 

CREATE TABLE `employeeaudit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(45) DEFAULT NULL,
  `accountid` int DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `forwhat` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` float DEFAULT NULL,
  `previousbalance` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`employeeid`) REFERENCES employee (id),
  FOREIGN KEY (`accountid`) REFERENCES account (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the employee balance update audit function';

ALTER TABLE `antrackerdb`.`customeraudit` 
CHANGE COLUMN `forwhat` `operation` VARCHAR(45) NULL DEFAULT NULL ;


ALTER TABLE `antrackerdb`.`customeraudit` 
CHANGE COLUMN `amount` `amount` FLOAT NULL DEFAULT NULL ;

ALTER TABLE `antrackerdb`.`employeeaudit` 
CHANGE COLUMN `forwhat` `operation` VARCHAR(45) NULL DEFAULT NULL ;



ALTER TABLE `antrackerdb`.`customeraudit` ADD COLUMN `date` datetime  default CURRENT_TIMESTAMP ;
ALTER TABLE `antrackerdb`.`employeeaudit` ADD COLUMN `date` datetime  default CURRENT_TIMESTAMP ;

ALTER TABLE `antrackerdb`.`employeeaudit` 
ADD COLUMN `forwhat` VARCHAR(30) NULL AFTER `date`;



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
    IN operation varchar(15)
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
insert into customeraudit(`customerid`,`accountid`,`amount`,`operation`,`otherparty`,`newbalance`,`previousbalance`,`type`)
 values (customerid,accountid,amount,operation, otherparty,newcurrentBalance,newlastbalance,txntype);
END$$

DELIMITER ;

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
    IN forwhat varchar(30)
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
insert into employeeaudit(`employeeid`,`accountid`,`amount`,`operation`,`otherparty`,`newbalance`,`previousbalance`,`type`,`forwhat`)
 values (employeeid,accountid,amount,operation, otherparty,newcurrentBalance,newlastbalance,txntype,forwhat);
END$$

DELIMITER ;



