use `antrackerdb`;
DROP TABLE IF EXISTS `p_gst_report`;
CREATE TABLE `p_gst_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgid` int(11) NOT NULL,
  `toemails` varchar(500) DEFAULT NULL,
  `generatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `pdffilepath` varchar(256) DEFAULT NULL,
  `excelfilepath` varchar(256) DEFAULT NULL,
  `fromemail` varchar(100) DEFAULT NULL,
  `mode` varchar(20) DEFAULT NULL,
  `reportstatus` varchar(20) DEFAULT NULL,
  `formonth` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `p_gst_reports_ibfk_1_idx` (`orgid`),
  CONSTRAINT `p_gst_reports_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
)