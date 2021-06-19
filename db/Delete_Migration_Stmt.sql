select * from antrackerdb.organization;

use antrackerdb
SET SQL_SAFE_UPDATES=0;
SET SQL_SAFE_DELETES = 0;
delete from customer where id not in (58);

delete from customerinvoice where orgid <> 58
delete from delivery where orgid <> 58
delete from generalexpense where orgid <> 58
delete from internaltransfer where orgid <> 58
delete from paymentreceived where orgid <> 58
delete from paytovendor where orgid <> 58
delete from purchasefromvendor where orgid <> 58
delete from retailsale where orgid <> 58
delete from customeraudit where orgid <> 58
delete from customer where orgid <> 58

delete from employeeaudit where orgid <> 58
delete from employeesalary where orgid <> 58
delete from employeesalarypayment where orgid <> 58
delete from internaltransfer where orgid <> 58
delete from employee_salary_audit where orgid <> 58
delete from employee where orgid <> 58
delete from `organization` where id <> 58
delete from phonebook_contact where orgid<>58
delete from phonebook where orgid<>58
delete from calculationtracker where orgid<>58

delete from organization where id<>58



