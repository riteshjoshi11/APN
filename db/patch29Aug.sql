
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('1', 'view.emp.personalbalance');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('2', 'view.emp.salary');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('3', 'view.dashboard');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('4', 'view.emp.account');

CREATE TABLE `role_permission` (
  `empTypeId` int(11) NOT NULL,
  `permissionId` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '2');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '3');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '4');




