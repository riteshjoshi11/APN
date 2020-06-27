USE `antrackerdb`;
DROP procedure IF EXISTS `SoftDeleteOrgData_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `SoftDeleteOrgData_Procedure`(
	IN ParamOrgId INT,
    IN deleteCompanyData boolean,
    IN deleteSalaryData boolean,
    IN deleteAuditData boolean,
    IN deleteEmployeeSalaryBalance boolean,
    IN deleteEmployeeCompanyBalance  boolean,
    IN deleteCustomerBalance boolean
)
BEGIN
SET SQL_SAFE_UPDATES = 0;
-- Setting all the transaction tables to softDeleted state

If(deleteCompanyData = true) then
	update customerinvoice SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update delivery SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update generalexpense SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update internaltransfer SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update paymentreceived SET `isdeleted` = true , `deletedate`= CURDATE() where orgid =ParamOrgId;
	update paytovendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update purchasefromvendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update retailsale SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	
	-- Taking backup to restore later (if required)
	update calculationtracker  SET `lastunpaidexpense` = `unpaidexpense`, `lastpaidexpense` = `paidexpense`, `unpaidexpense`=0, `paidexpense`=0  where orgid = ParamOrgId;
	
End if;

IF(deleteSalaryData = true) then
	update employeesalarypayment SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update employeesalary SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;

IF(deleteAuditData = true) then
	update employeeaudit SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update customeraudit SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;

-- Deleting the balances, the currentbalance will be copied over to lastbalance and currentbalance will be set to 0

If(deleteEmployeeSalaryBalance = true) then
   update employee  SET `lastsalarybalance` = `currentsalarybalance`, `currentsalarybalance` = 0 where orgid = ParamOrgId;
END if;

If(deleteEmployeeCompanyBalance = true) then
	update `account` SET `lastbalance` = `currentbalance`, `currentbalance` = 0  where type = 'Employee' and orgid = ParamOrgId;
END if;

If(deleteCustomerBalance = true) then
	update `account` SET `lastbalance` = `currentbalance`, `currentbalance` = 0 where type = 'Customer' and orgid = ParamOrgId;
END if;

End$$

DELIMITER ;


USE `antrackerdb`;
DROP procedure IF EXISTS `antrackerdb`.`Test_reverse_delete_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
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
;

ALTER TABLE `antrackerdb`.`calculationtracker` 
ADD COLUMN `lastunpaidexpense` FLOAT NULL AFTER `totalpaidsalary`,
ADD COLUMN `lastpaidexpense` FLOAT NULL AFTER `lastunpaidexpense`;
