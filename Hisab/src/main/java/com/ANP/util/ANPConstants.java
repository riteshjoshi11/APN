package com.ANP.util;

public interface ANPConstants {
      public static final int EMPLOYEE_TYPE_SUPER_ADMIN=1;
      public static final int EMPLOYEE_TYPE_VIRTUAL=6;
      public static final int EMPLOYEE_TYPE_DEFAULT=7;


      public static final String LOGIN_TYPE_EMPLOYEE="EMPLOYEE";
      public static final String LOGIN_TYPE_CUSTOMER="CUSTOMER";

      public static final String SEARCH_FIELDTYPE_STRING="String" ;
      public static final String SEARCH_FIELDTYPE_DATE="Date" ;

      public  static final String AUDIT_TYPE_INITIAL_BALANCE="InitialBalance";
      public  static final String CUSTOMER_AUDIT_TYPE_SALE="Sale";
      public  static final String CUSTOMER_AUDIT_TYPE_PURCHASE="Purchase";
      public  static final String CUSTOMER_AUDIT_TYPE_PAYTOVENDOR="PayToVendor";
      public  static final String CUSTOMER_AUDIT_TYPE_PAYMENTRCVDFROMVENDOR="PaymentReceivedFromVendor";

      public  static final String EMPLOYEE_AUDIT_TYPE_PAY="Pay";
      public  static final String EMPLOYEE_AUDIT_TYPE_RCVD="Receive";
      public  static final String EMPLOYEE_AUDIT_FORWHAT_SALARYPAY="Salary";
      public  static final String EMPLOYEE_AUDIT_FORWHAT_RETAILSALE="Retail";
      public  static final String EMPLOYEE_AUDIT_FORWHAT_EXPENSE="Expense";
      public  static final String EMPLOYEE_AUDIT_FORWHAT_VENDORPAY="VendorPay";
      public  static final String EMPLOYEE_AUDIT_FORWHAT_CUSTOMER_RCVD="Customer";
      public  static final String EMPLOYEE_AUDIT_FORWHAT_INTERNAL="Internal";

      public  static final String OPERATION_TYPE_ADD="ADD" ;
      public  static final String OPERATION_TYPE_SUBTRACT="SUBTRACT";



}
