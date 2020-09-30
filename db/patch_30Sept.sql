USE `antrackerdb`;
DROP procedure IF EXISTS `SoftDeleteOrgData_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `SoftDeleteOrgData_Procedure`(
	IN RetainGSTTransaction boolean,
	IN RetainStaffSalaryTransaction boolean,
	IN RetainStaffAccountBalance boolean,
    IN RetainCustomerBalance boolean,
    IN RetainStaffSalaryBalance boolean,

    IN ParamOrgId LONG
)
BEGIN
	update delivery SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId;
	update internaltransfer SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update paymentreceived SET `isdeleted` = true , `deletedate`= CURDATE() where orgid =ParamOrgId;
	update paytovendor SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update retailsale SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;

if(RetainGSTTransaction = true) then
	update `generalexpense` SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId and includeinreport = false and includeincalc = false;
	update `customerinvoice` SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId and includeinreport = false and includeincalc = false;
	update `purchasefromvendor` SET `isdeleted` = true, `deletedate`= CURDATE() where orgid = ParamOrgId and includeinreport = false and includeincalc = false;
 END if;
IF(RetainStaffSalaryTransaction = true) then
	update employeesalarypayment SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
	update employeesalary SET `isdeleted` = true , `deletedate`= CURDATE() where orgid = ParamOrgId;
END if;
If(RetainStaffSalaryBalance = true) then
   update employee  SET `lastsalarybalance` = `currentsalarybalance`, `currentsalarybalance` = 0  where orgid = ParamOrgId;
END if;
If(RetainStaffAccountBalance = true) then
	update `account` SET `lastbalance` = `currentbalance`, `currentbalance` = 0 where type = 'EMPLOYEE' and orgid = ParamOrgId;
END if;
If(RetainCustomerBalance = true) then
	update `account` SET `lastbalance` = `currentbalance`, `currentbalance` = 0 where type = 'CUSTOMER' and orgid = ParamOrgId;
END if;
End$$

DELIMITER ;

