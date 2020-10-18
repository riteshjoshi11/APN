USE `antrackerdb`;
DROP procedure IF EXISTS `ControlOrgDataGrowthTransactionWise_Procedure`;
DROP procedure IF EXISTS `InternalDataControl_Procedure`;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `ControlOrgDataGrowthTransactionWise_Procedure`(
IN RegularNoOfTxn Int,
IN PremiumNoOFTxn Int,
IN TableName Varchar(75)
)
BEGIN
declare iterator int;
declare countTempRows int;

SET @sql2 = CONCAT('CREATE TEMPORARY TABLE IF NOT EXISTS tempotable AS select count(id) , orgid mainorgid , (select id from org_subscription t where t.orgid=mainOrgID)
 from ', TableName ,' group by orgid having (count(id)> ',RegularNoOfTxn,' and orgid NOT IN (select orgid from org_subscription) )
 or (count(id)>',PremiumNoOFTxn,' and orgid IN(select orgid from org_subscription) ) ;');
PREPARE stmt from @sql2;
EXECUTE stmt;
select count(*) from tempotable into countTempRows;
set iterator = 0;
while(iterator<countTempRows) do
	set @sql3 = CONCAT('Select * into @count,@ParamOrgId,@isPremium from tempotable limit 1 offset ',iterator,';');
	PREPARE stmt from @sql3;
	EXECUTE stmt;
	call InternalDataControl_Procedure (TableName,RegularNoOfTxn,PremiumNoOFTxn,@ParamOrgId,@count,@isPremium);
  set iterator = iterator +1;
end while;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `InternalDataControl_Procedure`(
IN TableName Varchar(75),
IN RegularNoOfTxn Int,
IN PremiumNoOFTxn Int,
IN ParamOrgId long,
IN count Int,
In isPremium long
)
BEGIN
if(isPremium is  null) then
	if(count > RegularNoOfTxn) then
		SET @sql = CONCAT('update ',TableName,' set `isdeleted` = true, `deletedate` = curdate()
		where orgid = ', ParamOrgId, ' order by createdate LIMIT ',(count - RegularNoOfTxn),';');
   end if;
else
	if(count > PremiumNoOFTxn) then
		SET @sql = CONCAT('update ',TableName,' set `isdeleted` = true, `deletedate` = curdate()
		where orgid = ', ParamOrgId, ' order by createdate LIMIT ',(count - PremiumNoOFTxn),';');
    end if;
End if;
PREPARE stmt1 from @sql;
EXECUTE stmt1 ;
END$$
DELIMITER ;

