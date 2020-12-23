package com.ANP.util;

public interface ANPConstants {
     // public static final int EMPLOYEE_TYPE_SUPER_ADMIN=1;
     // public static final int EMPLOYEE_TYPE_VIRTUAL=6;
      //public static final int EMPLOYEE_TYPE_DEFAULT=7;


      public static final String LOGIN_TYPE_EMPLOYEE="EMPLOYEE";
      public static final String LOGIN_TYPE_CUSTOMER="CUSTOMER";

      public static final String SEARCH_FIELDTYPE_STRING="String" ;
      public static final String SEARCH_FIELDTYPE_DATE="Date" ;

      public  static final String OPERATION_TYPE_ADD="ADD" ;
      public  static final String OPERATION_TYPE_SUBTRACT="SUBTRACT";

      public static final String DB_TBL_customerinvoice = "customerinvoice";
      public static final String DB_TBL_purchasefromvendor = "purchasefromvendor";
      public static final String DB_TBL_delivery = "delivery";
      public static final String DB_TBL_generalexpense = "generalexpense";
      public static final String DB_TBL_internaltransfer = "internaltransfer";
      public static final String DB_TBL_paymentreceived = "paymentreceived";
      public static final String DB_TBL_paytovendor = "paytovendor";
      public static final String DB_TBL_retailsale = "retailsale";
      public static final String DB_TBL_GST_REPORT = "p_gst_reports";
      public static final String DB_TBL_TXN_REPORT = "p_txn_reports";
      public static final String DB_TBL_UI_OBJ_COMAPANYTYPE = "companytype" ;
      public static final String DB_TBL_UI_OBJ_BUSINESS_NATURE="businessnature" ;
      public static final String DB_TBL_UI_OBJ_NOOFEMPLOYEES="noofemployee";


      public static final String DB_TBL_UI_OBJ_EMPLOYEE_TYPE="employeetype";

      public static final String TRANSACTION_NAME_SALE = "SALE";
      public static final String TRANSACTION_NAME_PURCHASE = "PURCHASE";
      public static final String TRANSACTION_NAME_PAYMENT_RECEIVED = "PAYMENT_RECEIVED";
      public static final String TRANSACTION_NAME_PAY_TO_VENDOR = "PAY_TO_VENDOR";
      public static final String TRANSACTION_NAME_EXPENSE = "EXPENSE";
      public static final String TRANSACTION_NAME_INTERNAL_TRANSFER = "INTERNAL_TRANSFER";
      public static final String TRANSACTION_NAME_RETAIL_SALE = "RETAIL_SALE";


      public static final String SYSTEM_AUDIT_CUSTOMER = "CUSTOMER";
      public static final String SYSTEM_AUDIT_EMPLOYEE = "EMPLOYEE";

      public static final String GST_REPORT_CURRENT_MONTH="current";
      public static final String GST_REPORT_PREVIOUS_MONTH="previous";

      public  static final String EMPLOYEE_SALARY_AUDIT_TYPE_PAY="Salary_Pay";
      public  static final String EMPLOYEE_SALARY_AUDIT_TYPE_DUE="Salary_Due";

}
