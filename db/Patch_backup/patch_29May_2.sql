USE `antrackerdb`;
DROP procedure IF EXISTS `TotalCashInHand_Procedure`;

DELIMITER $$
USE `antrackerdb`$$
CREATE DEFINER=`root`@`%` PROCEDURE `TotalCashInHand_Procedure`(
	IN ParamOrgId INT,
    OUT totalCashInHand float
	)
BEGIN
SELECT IF(sum(CurrentBalance) IS NULL, 0 , sum(CurrentBalance) ) INTO totalCashInHand FROM account WHERE OrgId= ParamOrgId and type='Employee' ;
END$$

DELIMITER ;