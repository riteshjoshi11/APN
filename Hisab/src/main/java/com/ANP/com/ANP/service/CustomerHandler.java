package com.ANP.com.ANP.service;

import com.ANP.repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerHandler {
    @Autowired
    CustomerDao customerDao;


}
