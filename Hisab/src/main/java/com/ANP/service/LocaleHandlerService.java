package com.ANP.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocaleHandlerService {

    Map<String,String> employeeAuditTypeMap = null;

    //Initializer block
    {
        System.out.println("Initializing Map...");
        //Look at also EmployeeAuditBean.TRANSACTION_TYPE_ENUM
        employeeAuditTypeMap = new HashMap<String, String>();
        employeeAuditTypeMap.put("Salary", new String("Salary"));
        employeeAuditTypeMap.put("Retail", new String("Retail"));
        employeeAuditTypeMap.put("Expense", new String("Expense"));
        employeeAuditTypeMap.put("Supplier", new String("Supplier"));
        employeeAuditTypeMap.put("Customer", new String("Customer"));
        employeeAuditTypeMap.put("Staff_Transfer", new String("Staff_Transfer"));
    }


    public String getEmployeeAuditType(String lang, String code) {
        if(employeeAuditTypeMap!=null) {
            return employeeAuditTypeMap.get(code);
        }
        return null;
    }
}
