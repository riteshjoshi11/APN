CREATE TABLE `antrackerdb`.`retailsale` (
  `id` INT NOT NULL auto_increment,
  `notes` VARCHAR(150) NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `fromemployeeid` VARCHAR(25) NOT NULL,
  `fromaccountid` INT NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = 'This table will track the retail sale';


ALTER TABLE `antrackerdb`.`retailsale` 
ADD INDEX `fromaccountid_idx` (`fromaccountid` ASC) VISIBLE,
ADD INDEX `fromemployeeid_idx` (`fromemployeeid` ASC) VISIBLE;
;
ALTER TABLE `antrackerdb`.`retailsale` 
ADD CONSTRAINT `fromaccountid`
  FOREIGN KEY (`fromaccountid`)
  REFERENCES `antrackerdb`.`account` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fromemployeeid`
  FOREIGN KEY (`fromemployeeid`)
  REFERENCES `antrackerdb`.`employee` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  
  ALTER TABLE `antrackerdb`.`retailsale` 
ADD COLUMN `orgid` INT NULL AFTER `fromaccountid`,
ADD COLUMN `createdbyid` VARCHAR(25) NULL AFTER `orgid`,
ADD COLUMN `date` DATETIME NULL AFTER `createdbyid`;

ALTER TABLE `antrackerdb`.`retailsale` 
ADD COLUMN `includeincalc` TINYINT NULL AFTER `date`,
ADD COLUMN `createdate` DATETIME NULL AFTER `includeincalc`;


ALTER TABLE `antrackerdb`.`retailsale` 
CHANGE COLUMN `createdate` `createdate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

