CREATE TABLE `antrackerdb`.`otphandler` (
  `mobileNumber` VARCHAR(15) NOT NULL,
  `otp` VARCHAR(8) NULL,
  `generatedtime` DATETIME NULL,
  PRIMARY KEY (`mobileNumber`));

ALTER TABLE `antrackerdb`.`otphandler` 
CHANGE COLUMN `otp` `otp` VARCHAR(8) NOT NULL ,
CHANGE COLUMN `generatedtime` `generatedtime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;


ALTER TABLE `antrackerdb`.`generalexpense` 
ADD COLUMN `topartygstno` VARCHAR(20) NULL AFTER `extra`,
ADD COLUMN `topartymobileno` VARCHAR(15) NULL AFTER `topartygstno`;

INSERT INTO `antrackerdb`.`city` (`id`, `name`) VALUES ('1', 'Pune');
INSERT INTO `antrackerdb`.`city` (`id`, `name`) VALUES ('2', 'Indore');
INSERT INTO `antrackerdb`.`city` (`id`, `name`) VALUES ('3', 'Jaipur');

