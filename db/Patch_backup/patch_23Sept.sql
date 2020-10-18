use antrackerdb;
ALTER TABLE `antrackerdb`.`customerinvoice`
CHANGE COLUMN `cgst` `cgst` DECIMAL(30,2) NULL DEFAULT '0' ;