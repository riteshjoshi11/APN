use antrackerdb;

ALTER TABLE `antrackerdb`.`employee` 
ADD COLUMN `mobile2` VARCHAR(20) NULL AFTER `createdate`;

CREATE TABLE `employee_salary_audit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employeeid` varchar(45) DEFAULT NULL,
  `type` varchar(25) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `operation` varchar(45) DEFAULT NULL,
  `otherparty` varchar(80) DEFAULT NULL,
  `newbalance` float DEFAULT NULL,
  `previousbalance` float DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `orgid` int(11) NOT NULL,
  `txndate` datetime DEFAULT NULL,
  `isdeleted` tinyint(4) DEFAULT NULL,
  `deletedate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employeeid` (`employeeid`),
  CONSTRAINT `employee_salary_audit_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`),
  CONSTRAINT `employee_salary_audit_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table tracks the employee Salary balance update'
  
  
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `UpdateEmployeeSalaryBalanceWithAudit_Procedure`(
	IN employeeid varchar(25),
    IN orgid bigint,
	IN amount double,
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
