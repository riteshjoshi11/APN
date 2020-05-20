ALTER TABLE `antrackerdb`.`customer` 
DROP FOREIGN KEY `customer_ibfk_2`;
ALTER TABLE `antrackerdb`.`customer` 
DROP INDEX `createdbyid` ;
;

INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Bank Loan');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Transporting');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Weight Lifting');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Miscellaneous');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Tea Coffee - Refreshment');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Marketing');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Electricity Bill');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Other Bill');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Other');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Repairing');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Daily Wages');
INSERT INTO `antrackerdb`.`expense_cat` (`name`) VALUES ('Small Purchase');
