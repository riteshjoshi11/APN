use antrackerdb;
ALTER TABLE `antrackerdb`.`phonebook`
CHANGE COLUMN `lastsyncdate` `lastsyncdate` TIMESTAMP NULL DEFAULT NULL ;
