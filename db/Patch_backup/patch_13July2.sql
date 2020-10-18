USE `antrackerdb`;
ALTER TABLE `antrackerdb`.`noofemployee`
CHANGE COLUMN `range` `name` VARCHAR(100) NULL DEFAULT NULL ;