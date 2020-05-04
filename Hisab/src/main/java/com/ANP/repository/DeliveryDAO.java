package com.ANP.repository;

import com.ANP.bean.DeliveryBean;
import com.ANP.bean.EmployeeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public boolean createDelivery(DeliveryBean deliveryBean) {
        boolean result = false;
        int accountCreated =
         namedParameterJdbcTemplate.update(
                "insert into employee (tocustomerid,description,date,orgid,createdbyid,createdate" +
                        " values(:toCustomerID,:description,date,orgId,createdbyId,createDate)",
                        new BeanPropertySqlParameterSource(deliveryBean));
        if (accountCreated > 0) {
            result = true;
        }
        return result;
    }

}
