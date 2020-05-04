ALTER TABLE `antrackerdb`.`organization` 
DROP COLUMN `companyid`,
ADD COLUMN `state` VARCHAR(100) NULL AFTER `orgname`,
ADD COLUMN `city` VARCHAR(100) NULL AFTER `state`;


USE `antrackerdb`;

DELIMITER $$

USE `antrackerdb`$$
DROP TRIGGER IF EXISTS `antrackerdb`.`customer_BEFORE_INSERT` $$
DELIMITER ;


USE `antrackerdb`;

DELIMITER $$

USE `antrackerdb`$$
DROP TRIGGER IF EXISTS `antrackerdb`.`employee_BEFORE_INSERT` $$
DELIMITER ;
USE `antrackerdb`;

DELIMITER $$

USE `antrackerdb`$$
DROP TRIGGER IF EXISTS `antrackerdb`.`employee_BEFORE_UPDATE` $$
DELIMITER ;


USE `antrackerdb`;
DROP function IF EXISTS `getcustomerId`;

DELIMITER $$
USE `antrackerdb`$$
CREATE FUNCTION `getcustomerId` ()
RETURNS  varchar(15)
BEGIN
declare customerId varchar(10);
INSERT INTO customer_seq VALUES (NULL);
SET customerId=CONCAT('C', LAST_INSERT_ID());
RETURN customerId;
END$$

DELIMITER ;


USE `antrackerdb`;
DROP function IF EXISTS `getEmployeeId`;

DELIMITER $$
USE `antrackerdb`$$
CREATE FUNCTION `getEmployeeId` ()
RETURNS  varchar(15)
BEGIN
declare employeeId varchar(10);
INSERT INTO employee_seq VALUES (NULL);
SET employeeId=CONCAT('E', LAST_INSERT_ID());
RETURN employeeId;
END$$

DELIMITER ;