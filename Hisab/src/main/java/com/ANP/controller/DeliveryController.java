package com.ANP.controller;

import com.ANP.bean.DeliveryBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.repository.DeliveryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/delivery")
public class DeliveryController {

    @Autowired
    DeliveryDAO deliveryDAO;

    @PostMapping(path = "/createDelivery", produces = "application/json")
    public ResponseEntity createEmployee(@RequestBody DeliveryBean deliveryBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isDeliveryCreated = deliveryDAO.createDelivery(deliveryBean);
        if (isDeliveryCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

}
