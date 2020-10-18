use antrackerdb;
drop table if exists `phonebook_contact`, `phonebook`;

CREATE TABLE `phonebook` (
  `id` int NOT NULL AUTO_INCREMENT,
  `orgid` int DEFAULT NULL,
  `employeeid` varchar(25) DEFAULT NULL,
  `sync_status` varchar(15) DEFAULT NULL,
  `lastsyncdate` date,
  PRIMARY KEY (`id`),
  CONSTRAINT `phonebook_ibfk_2` FOREIGN KEY (`orgid`) REFERENCES `organization` (`id`),
  CONSTRAINT `phonebook_ibfk_3` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`id`)
);

CREATE TABLE `phonebook_contact` (
  `contact_name`      varchar(15) DEFAULT NULL,
  `key`       varchar(15) DEFAULT NULL,
  `value`     varchar(255) DEFAULT NULL,
  `isGlobal`  tinyint,
  `phonebookid`  int,
  `isdeleted` TINYINT,
  CONSTRAINT `phonebook_contact_ibfk_1` FOREIGN KEY (`phonebookid`) REFERENCES `phonebook` (`id`)
);



ALTER TABLE `antrackerdb`.`phonebook` 
COMMENT = 'This table is master for storing all the contacts coming from UI. This stores information like for which employee this address book is.' ;
ALTER TABLE `antrackerdb`.`phonebook_contact` 
COMMENT = 'Self- Idenpedent table for storing a contact. Key would be like mobile, website, or email and \'value\' will be value of the key. IsGlobal is mainly to designated whether the contact is under global category meaning can be shown to all. \'contact_name\' is name for that contact. ' ;
