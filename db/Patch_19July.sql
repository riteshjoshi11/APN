use antrackerdb;
drop table if exists `userphonebook`;
drop table if exists `userdetail`;
CREATE TABLE `userdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `userphonebook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userdetailid` int(11) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userphonebook_ibfk_1_idx` (`userdetailid`),
  CONSTRAINT `userphonebook_ibfk_1` FOREIGN KEY (`userdetailid`) REFERENCES `userdetail` (`id`)
);
