ALTER TABLE `antrackerdb`.`customer` 
ADD COLUMN `state` VARCHAR(45) NULL DEFAULT NULL AFTER `sendPaymentReminders`;
