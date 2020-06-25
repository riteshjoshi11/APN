USE `antrackerdb`;
DROP procedure IF EXISTS `DeleteOrganizationTransaction_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `DeleteOrganizationTransaction_Procedure`(
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

If(deleteCompanyData = true) then
	update customerinvoice SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update delivery SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update generalexpense SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update internaltransfer SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update paymentreceived SET `isdeleted` = true , `deletedate`= CURDATE() where orgid =ParamOrgId;
	update paytovendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update purchasefromvendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update retailsale SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
End if;
IF(deleteSalaryData = true) then
	update employeesalarypayment SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update employeesalary SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;

IF(deleteAuditData = true) then
	update employeeaudit SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update customeraudit SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;

If(deleteEmployeeSalaryBalance = true) then
   update employee  SET `currentsalarybalance` = 0, `lastsalarybalance` = 0 where orgid = ParamOrgId;
END if;
If(deleteEmployeeCompanyBalance = true) then
	update `account` SET `currentbalance` = 0, `lastbalance` = 0 where type = 'Employee' and orgid = ParamOrgId;
END if;
If(deleteCustomerBalance = true) then
	update `account` SET `currentbalance` = 0, `lastbalance` = 0 where type = 'Customer' and orgid = ParamOrgId;
END if;


End$$

DELIMITER ;

