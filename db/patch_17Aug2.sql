use antrackerdb;
ALTER TABLE `antrackerdb`.`account`
ADD COLUMN `initialbalance` FLOAT NULL AFTER `createdate`;

ALTER TABLE `antrackerdb`.`employee`
DROP COLUMN `loginusername`;

ALTER TABLE `antrackerdb`.`account`
CHANGE COLUMN `initialbalance` `initialbalance` FLOAT NULL DEFAULT '0' ;


