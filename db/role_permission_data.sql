INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('1', 'view.other.emp.personalbalance');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('2', 'view.other.emp.salarybalance');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('3', 'view.dashboard.company.details');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('4', 'view.emp.personalbalance.page');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('5', 'view.emp.salarybalance.page');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('6', 'view.delivery.page');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('7', 'view.gst.report.page');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('8', 'view.txn.report.page');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('9', 'view.txn.report.page');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('10', 'view.reset.clean.page');

INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('11', 'create.employee');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('12', 'create.customer');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('13', 'create.employee.fingerprint');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('14', 'authenticate.employee.fingerprint');
INSERT INTO `antrackerdb`.`permission` (`id`, `name`) VALUES ('15', 'view.other.account.selection');


## Super Admin
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '2');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '3');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '4');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '5');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '6');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '7');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '8');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '9');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '10');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '11');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '12');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '13');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '14');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('1', '15');



## Partner
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '2');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '3');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '4');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '5');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '6');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '7');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '8');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '9');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '11');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '12');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '13');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '14');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '15');


## Sales Person (3) 
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('3', '8');


## Accountant (4)
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '1');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '2');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '3');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '4');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '5');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '6');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '7');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '8');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('4', '9');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '11');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '12');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '13');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '14');
INSERT INTO `antrackerdb`.`role_permission` (`empTypeId`, `permissionId`) VALUES ('2', '15');
