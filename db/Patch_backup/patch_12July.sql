use antrackerdb;
DROP PROCEDURE IF EXISTS SoftDeleteAuditData_Procedure;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `SoftDeleteAuditData_Procedure`(
	IN ParamOrgId INT,
    IN RecordId LONG,
	IN Identifier varchar(15),
    IN DeleteAll boolean
)
BEGIN
	if(Identifier = 'EMPLOYEE') then
		If(DeleteAll = true and RecordId <=0) then
			update employeeaudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId;
		else if(DeleteAll = false and RecordId >0) then
			update employeeaudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId and `id` = RecordId;
        End if;
        End if;
	else 
		If(deleteAll = true and RecordId <=0) then
			update customeraudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId;
		else if(DeleteAll = false and RecordId >0) then
			update customeraudit set `isdeleted` = true, `deletedate` =  CURDATE() where `orgid` = ParamOrgId and `id` = RecordId;
        End if;
		End if;
    End if;
END$$
DELIMITER ;
