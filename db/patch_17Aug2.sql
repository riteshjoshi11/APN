use antrackerdb;
ALTER TABLE `antrackerdb`.`account`
ADD COLUMN `initialbalance` FLOAT NULL AFTER `createdate`;

ALTER TABLE `antrackerdb`.`employee`
DROP COLUMN `loginusername`;

