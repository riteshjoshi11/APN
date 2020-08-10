ALTER TABLE `antrackerdb`.`phonebook_contact`
ADD COLUMN `isdeleted` TINYINT(1) NULL AFTER `phonebookid`;