USE `antrackerdb`;
DROP procedure IF EXISTS `Test_reverse_delete_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `Test_reverse_delete_Procedure`(
	IN ParamOrgId INT,
    IN deleteCompanyData boolean,
    IN deleteSalaryData boolean,
    IN deleteAuditData boolean,
    IN deleteEmployeeSalaryBalance boolean,
    IN deleteEmployeeCompanyBalance  boolean,
    IN deleteCustomerBalance boolean
)
BEGIN
If(deleteCompanyData = true) then
	update customerinvoice SET `isdeleted` = true where orgid = ParamOrgId;
	update delivery SET `isdeleted` = true where orgid = ParamOrgId;
	update generalexpense SET `isdeleted` = true where orgid = ParamOrgId;
	update internaltransfer SET `isdeleted` = true where orgid = ParamOrgId;
	update paymentreceived SET `isdeleted` = true  where orgid =ParamOrgId;
	update paytovendor SET `isdeleted` = true  where orgid = ParamOrgId;
	update purchasefromvendor SET `isdeleted` = true  where orgid = ParamOrgId;
	update retailsale SET `isdeleted` = true where orgid = ParamOrgId;
End if;
IF(deleteSalaryData = true) then
	update employeesalarypayment SET `isdeleted` = true  where orgid = ParamOrgId;
	update employeesalary SET `isdeleted` = true  where orgid = ParamOrgId;
END if;

IF(deleteAuditData = true) then
	update employeeaudit SET `isdeleted` = true  where orgid = ParamOrgId;
	update customeraudit SET `isdeleted` = true  where orgid = ParamOrgId;
END if;

If(deleteEmployeeSalaryBalance = true) then
   update employee  SET `currentsalarybalance` = 10, `lastsalarybalance` = 10 where orgid = ParamOrgId;
END if;
If(deleteEmployeeCompanyBalance = true) then
	update `account` SET `currentbalance` = 10, `lastbalance` = 10 where type = 'Employee' and orgid = ParamOrgId;
END if;
If(deleteCustomerBalance = true) then
	update `account` SET `currentbalance` = 10, `lastbalance` = 10 where type = 'Customer' and orgid = ParamOrgId;
END if;


End$$

DELIMITER ;

