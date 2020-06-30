use `antrackerdb`;
SET SQL_SAFE_UPDATES = 0;
delete  from companytype;
INSERT INTO `companytype` VALUES (1,'One Person Company'),(2,'Private Ltd Company'),(3,'Public Ltd Company'),(4,'Limited Liability Partnership (LLP) '),(5,'Proprietorship / HUF'),(6,'Partnership'),(7,'Cooperatives');
