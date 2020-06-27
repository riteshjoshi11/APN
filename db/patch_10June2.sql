ALTER TABLE `antrackerdb`.`account` 
CHANGE COLUMN `currentbalance` `currentbalance` FLOAT NULL DEFAULT 0 ;

ALTER TABLE `antrackerdb`.`account` 
CHANGE COLUMN `lastbalance` `lastbalance` FLOAT NULL DEFAULT 0 ;