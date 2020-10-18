CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteOrganizationTransaction_Procedure`(
	IN ParamOrgId INT,
    IN deleteCompanyData boolean,
    IN deleteSalaryData boolean,
    IN deleteAuditData boolean
)
BEGIN
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
End


ALTER TABLE `antrackerdb`.`customerinvoice`
ADD COLUMN `isdeleted` TINYINT NULL AFTER,
ADD COLUMN `deletedate` DATE NULL AFTER;

ALTER TABLE `antrackerdb`.`delivery`
ADD COLUMN `isdeleted` TINYINT NULL AFTER,
ADD COLUMN `deletedate` DATE NULL AFTER;

ALTER TABLE `antrackerdb`.`generalexpense`
ADD COLUMN `isdeleted` TINYINT NULL,
ADD COLUMN `deletedate` DATE NULL;

ALTER TABLE `antrackerdb`.`internaltransfer`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;

ALTER TABLE `antrackerdb`.`paymentreceived`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;


ALTER TABLE `antrackerdb`.`paytovendor`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;

ALTER TABLE `antrackerdb`.`purchasefromvendor`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;

ALTER TABLE `antrackerdb`.`retailsale`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;


ALTER TABLE `antrackerdb`.`employeesalary`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;


ALTER TABLE `antrackerdb`.`employeesalarypayment`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;


ALTER TABLE `antrackerdb`.`customeraudit`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;


ALTER TABLE `antrackerdb`.`employeeaudit`
ADD COLUMN `isdeleted` TINYINT NULL ,
ADD COLUMN `deletedate` DATE NULL ;
