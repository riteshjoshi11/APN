INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('1', 'view.emp.personalbalance');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('2', 'view.emp.salary');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('3', 'view.dashboard');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('4', 'view.emp.account');

INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '2');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '3');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '4');