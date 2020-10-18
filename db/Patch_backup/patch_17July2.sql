use antrackerdb;
drop table if exists `org_subscription`;
CREATE TABLE `org_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_subscription_ibfk1_idx` (`orgid`),
  CONSTRAINT `org_subscription_ibfk_1` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`)
)