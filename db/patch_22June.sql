DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `PurgeProcess_Procedure`(
IN NoOfDaysAfterDeletion INT,
IN PurgeArchiveTableName VARCHAR(50)
)
BEGIN
	SET @sql = CONCAT('insert into ',PurgeArchiveTableName,'_archive  (select * from ',PurgeArchiveTableName,' where isdeleted = true and deletedate >= date(now()) + interval -', NoOfDaysAfterDeletion ,' day) ;');
    PREPARE stmt1 from @sql;
    EXECUTE stmt1 ;

    set @sql2 = CONCAt ('delete from ', PurgeArchiveTableName, ' where isdeleted = true and deletedate >= date(now()) + interval -', NoOfDaysAfterDeletion ,' day ;');
	PREPARE stmt2 from @sql2;
    EXECUTE stmt2 ;
END$$
DELIMITER ;
