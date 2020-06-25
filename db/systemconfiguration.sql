use `antrackerdb`;
delete  from systemconfigurations;
insert into `systemconfigurations`(`key`,`value`) values('ArchivePurge.archiveandpurgetablelist','retailsale,purchasefromvendor,paytovendor,paymentreceived,internaltransfer,generalexpense,employeesalarypayment,employeesalary,employeeaudit,delivery,customerinvoice,customeraudit');
insert into `systemconfigurations`(`key`,`value`) values('ArchivePurge.DeleteAfterNumberOfDays',15);