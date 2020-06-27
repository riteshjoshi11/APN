use antrackerdb;
create table retailsale_archive like retailsale;
create table purchasefromvendor_archive like purchasefromvendor;
create table paytovendor_archive like paytovendor;
create table paymentreceived_archive like paymentreceived;
create table internaltransfer_archive like internaltransfer;
create table generalexpense_archive like generalexpense;
create table employeesalarypayment_archive like employeesalarypayment;
create table employeesalary_archive like employeesalary;
create table employeeaudit_archive like employeeaudit;
create table delivery_archive like delivery;
create table customerinvoice_archive like customerinvoice;
create table customeraudit_archive like customeraudit;

CREATE TABLE `systemconfigurations` (
  `key` varchar(75) DEFAULT 'null',
  `value` varchar(200) DEFAULT 'null'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into systemconfigurations(`key`) values('ArchiveAndPurgeTableList');