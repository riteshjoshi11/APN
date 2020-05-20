package com.ANP.repository;

import com.ANP.bean.PayToVendorBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PayToVendorDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createPayToVendor(PayToVendorBean payToVendorBean) {
        return namedParameterJdbcTemplate.update(
                "insert into paytovendor(fromaccountid,fromemployeeid,toaccountid,tocustomerid,date,amount,details,includeincalc,orgid,createdbyid)" +
                        " values(:fromAccountID,:fromEmployeeID,:toAccountID,:toCustomerID,:paymentDate,:amount,:details,:includeInCalc,:orgId,:createdbyId)",
                new BeanPropertySqlParameterSource(payToVendorBean));


    }


}
