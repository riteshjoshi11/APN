USE `antrackerdb`;
DROP procedure IF EXISTS `ControlOrgDataGrowthDateWise_Procedure`;
DROP procedure IF EXISTS `ControlOrgDataGrowthTransactionWise_Procedure`;
DROP procedure IF EXISTS `InternalDataControl_Procedure`;


DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `ControlOrgDataGrowthDateWise_Procedure`(
IN TableName Varchar(75),
IN RegularDaysAfterDelete Int,
IN PremiumDaysAfterDelete Int
)
BEGIN

SET @sql = CONCAT('update ',TableName,' set `isdeleted` = true, `deletedate` = curdate() where `orgid` not in (select `orgid` from `org_subscription`) and
date_sub(curdate(), Interval ',RegularDaysAfterDelete,' day) > `createdate` or (orgid in(select `orgid` from `org_subscription`) and
date_sub(curdate(), Interval ',PremiumDaysAfterDelete, ' day)> `createdate`);');

PREPARE stmt from @sql;
EXECUTE stmt ;

END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `ControlOrgDataGrowthTransactionWise_Procedure`(
IN RegularTransactionsAfterDelete Int,
IN PremiumTransactionsAfterDelete Int,
IN TableName Varchar(75)
)
BEGIN
declare count bigint;
declare ParamOrgId long;

declare FirstCursor cursor for select `organization`.`id`
FROM  `organization`
where
`organization`.`id` not in
(select `orgid` from `org_subscription`);

declare SecondCursor cursor for select `orgid` from `org_subscription`;
set count = (select count(*) from `organization` where organization.id not in(select id from org_subscription));
open FirstCursor;
     while(count>0) do
         fetch FirstCursor into ParamOrgId;
         call InternalDataControl_Procedure (TableName,RegularTransactionsAfterDelete,ParamOrgId);
         set count = count - 1;
	end while;
close FirstCursor ;
set count = (select count(*) from `org_subscription`);
open SecondCursor;
	while(count>0) do
         fetch SecondCursor into ParamOrgId;
         call InternalDataControl_Procedure (TableName,PremiumTransactionsAfterDelete,ParamOrgId);
         set count = count - 1;
	end while;
close SecondCursor ;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `InternalDataControl_Procedure`(
IN TableName Varchar(75),
IN AutomaticDeleteOnNoOfTransactions Int,
IN ParamOrgId long
)
BEGIN
SET @sql = CONCAT('update ', TableName,
' set `isdeleted` = true, `deletedate` = curdate()
where orgid = ', ParamOrgId ,' AND id IN ( select * from (
SELECT id FROM ', TableName , ' where orgid = ',ParamOrgId,
' order by createdate DESC
LIMIT ', AutomaticDeleteOnNoOfTransactions,',17476744073723451615) as pseudotable);');

PREPARE stmt1 from @sql;
EXECUTE stmt1 ;
END$$
DELIMITER ;


