use `antrackerdb`;
SET SQL_SAFE_UPDATES = 0;
delete from employeetype;
INSERT INTO employeetype VALUES (1,'SUPER_ADMIN'),(2,'partner'),(3,'sales'),(4,'labour'),(5,'accountant');