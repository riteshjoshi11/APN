use antrackerdb;
drop table if exists `phonebook`;
drop table if exists `contact`;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile1` varchar(15) DEFAULT NULL,
  `mobile2` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `phonebook` (
  `id` int(11) NOT NULL,
  `orgid` int(11) DEFAULT NULL,
  `contactid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phonebook_ibfk_1_idx` (`contactid`),
  CONSTRAINT `phonebook_ibfk_1` FOREIGN KEY (`contactid`) REFERENCES `contact` (`id`)
);
