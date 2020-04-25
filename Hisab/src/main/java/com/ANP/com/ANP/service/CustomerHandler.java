package com.ANP.com.ANP.service;

import com.ANP.repository.CustomerDao;
import com.ANP.bean.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerHandler {
    @Autowired
    CustomerDao customerDao;

    public boolean createCustomer(Customer customerBean) {
        //TODO: Joshi : create a customer in the database
        // also create an account into the database.
        // Make sure to have transaction.
        // read the logic to generate accountNickName in the backend document.
        return false;
    }
}
