USE `antrackerdb`;
DROP procedure IF EXISTS `SoftDeleteOrgData_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `SoftDeleteOrgData_Procedure`(
	IN ParamOrgId INT,
    IN DeleteCompanyData boolean,
    IN DeleteSalaryData boolean,
    IN DeleteEmployeeSalaryBalance boolean,
    IN DeleteEmployeeCompanyBalance  boolean,
    IN DeleteCustomerBalance boolean
)
BEGIN
If(DeleteCompanyData = true) then
	update customerinvoice SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update delivery SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update generalexpense SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update internaltransfer SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update paymentreceived SET `isdeleted` = true , `deletedate`= CURDATE() where orgid =ParamOrgId;
	update paytovendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update purchasefromvendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update retailsale SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
End if;
IF(DeleteSalaryData = true) then
	update employeesalarypayment SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update employeesalary SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;


If(DeleteEmployeeSalaryBalance = true) then
   update employee  SET `currentsalarybalance` = 0, `lastsalarybalance` = 0 where orgid = ParamOrgId;
END if;
If(DeleteEmployeeCompanyBalance = true) then
	update `account` SET `currentbalance` = 0, `lastbalance` = 0 where type = 'Employee' and orgid = ParamOrgId;
END if;
If(DeleteCustomerBalance = true) then
	update `account` SET `currentbalance` = 0, `lastbalance` = 0 where type = 'Customer' and orgid = ParamOrgId;
END if;


End$$

DELIMITER ;

