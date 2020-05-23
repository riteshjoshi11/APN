package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.DeliveryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/delivery")
public class DeliveryController {

    @Autowired
    DeliveryDAO deliveryDAO;

    @PostMapping(path = "/createDelivery", produces = "application/json")
    public ResponseEntity createDelivery(@RequestBody DeliveryBean deliveryBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isDeliveryCreated = deliveryDAO.createDelivery(deliveryBean);
        if (isDeliveryCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/listDeliveriesPaged", produces = "application/json")
    public List<DeliveryBean> listDeliveriesPaged(@RequestBody ListParametersBean listParametersBean) {
        return deliveryDAO.listDeliveriesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());

    }





}
