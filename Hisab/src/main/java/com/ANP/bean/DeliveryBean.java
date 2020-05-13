package com.ANP.bean;

import java.util.Date;

public class DeliveryBean extends  CommonAttribute {
    private long deliveryID;
    private java.util.Date date;
    private String toCustomerID;
    private String description;
    private CustomerBean customerBean;

    public DeliveryBean() {
        customerBean = new CustomerBean();
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public long getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(long deliveryID) {
        this.deliveryID = deliveryID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getToCustomerID() {
        return toCustomerID;
    }

    public void setToCustomerID(String toCustomerID) {
        this.toCustomerID = toCustomerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
