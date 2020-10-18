ALTER TABLE `antrackerdb`.`orgdetails` 
ADD COLUMN `autoemailgstreport` TINYINT(1) NULL AFTER `orgid`;


CREATE TABLE `antrackerdb`.`p_gst_reports` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orgid` INT NOT NULL,
  `pdffilepath` VARCHAR(256) NULL,
  `excelfilepath` VARCHAR(256) NULL,
  `generatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `generatedby` VARCHAR(150) NULL,
  `fromEmail` VARCHAR(100) NULL,
  `mode` VARCHAR(20) NULL,
  `reportstatus` VARCHAR(20) NULL,
  `formonth` VARCHAR(20) NULL,
   PRIMARY KEY (`id`),
   CONSTRAINT `p_gst_reports_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
  )
COMMENT = 'This table will store the gst related process data for all the organization';


CREATE TABLE `antrackerdb`.`p_gstrpt_send_email` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `processType` VARCHAR(20) NOT NULL,
  `p_gst_reports_id` INT,
  PRIMARY KEY (`id`),
  CONSTRAINT `p_gstrpt_send_email_fk` FOREIGN KEY (`p_gst_reports_id`) REFERENCES `p_gst_reports` (`id`)
)
COMMENT = 'This is a process table which will maintain a email sent of a report.';


ALTER TABLE `antrackerdb`.`orgdetails` 
ADD COLUMN `website` VARCHAR(2048) NULL AFTER `autoemailgstreport`;


ALTER TABLE `antrackerdb`.`orgdetails` 
DROP FOREIGN KEY `orgdetails_ibfk_4`,
DROP FOREIGN KEY `orgdetails_ibfk_3`,
DROP FOREIGN KEY `orgdetails_ibfk_2`;
ALTER TABLE `antrackerdb`.`orgdetails` 
DROP INDEX `orgdetails_ibfk_4_idx` ,
DROP INDEX `orgdetails_ibfk_3_idx` ,
DROP INDEX `orgdetails_ibfk_2_idx` ;
;

ALTER TABLE `antrackerdb`.`p_gstrpt_send_email` 
DROP COLUMN `processType`;
