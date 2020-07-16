USE `antrackerdb`;
DROP procedure IF EXISTS `ControlOrgDataGrowth_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `ControlOrgDataGrowth_Procedure`(
IN AutomaticDeleteAfterDays Int,
IN AutomaticDeleteOnNoOfTransactions Int
)
BEGIN

update customerinvoice set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(id) as records from (select id from customerinvoice) as pseudotable) > AutomaticDeleteOnNoOfTransactions;

update delivery set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from delivery) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update generalexpense set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from generalexpense) as pseudotable) > AutomaticDeleteOnNoOfTransactions;

update internaltransfer set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from internaltransfer) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update paymentreceived set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from paymentreceived) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update paytovendor set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from paytovendor) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update purchasefromvendor set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from purchasefromvendor) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update retailsale set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from retailsale) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update employeesalarypayment set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from employeesalarypayment) as pseudotable) > AutomaticDeleteOnNoOfTransactions;


update employeesalary set `isdeleted` = true, `deletedate` = curdate() where
date_sub(curdate(), Interval AutomaticDeleteAfterDays day) > `createdate` or
(select count(*) as records from (select * from employeesalary) as pseudotable) > AutomaticDeleteOnNoOfTransactions;



END$$

DELIMITER ;

