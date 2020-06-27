use `antrackerdb`;
DELIMITER //

DROP procedure IF EXISTS `PurgeProcess_Procedure`;

CREATE DEFINER=`root`@`%` PROCEDURE `PurgeProcess_Procedure`(
IN NoOfDaysAfterDeletion INT,
IN PurgeArchiveTableName VARCHAR(100)
)
BEGIN
	SET @sql = CONCAT('insert into ',PurgeArchiveTableName,'_archive  (select * from ',PurgeArchiveTableName,' where isdeleted = true and deletedate < date(now()) + interval -', NoOfDaysAfterDeletion ,' day) ;');
    PREPARE stmt1 from @sql;
    EXECUTE stmt1 ;

    set @sql2 = CONCAt ('delete from ', PurgeArchiveTableName, ' where isdeleted = true and deletedate < date(now()) + interval -', NoOfDaysAfterDeletion ,' day ;');
	PREPARE stmt2 from @sql2;
    EXECUTE stmt2 ;
END
//
DELIMITER ;