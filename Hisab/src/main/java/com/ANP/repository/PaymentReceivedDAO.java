package com.ANP.repository;

import com.ANP.bean.PaymentReceivedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentReceivedDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createPaymentReceived(PaymentReceivedBean paymentReceivedBean) {
        return namedParameterJdbcTemplate.update(
                "insert into paymentreceived (fromaccountid,fromcustomerid,toaccountid,toemployeeid,rcvddate,amount,details,includeincalc,orgid,createdbyid,createdate)" +
                        " values(:fromAccountID,:fromCustomerID,:toAccountID,:toEmployeeID,:receivedDate,:amount,:details,:includeInCalc,:orgId,:createdbyId,:createDate)",
                new BeanPropertySqlParameterSource(paymentReceivedBean));
    }
}
