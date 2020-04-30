package com.ANP.repository;

import com.ANP.bean.InternalTransferBean;
import com.ANP.bean.PaymentReceivedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InternalTransferDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int createInternalTransfer(InternalTransferBean internalTransferBean) {
        return namedParameterJdbcTemplate.update(
                "insert into internaltransfer (fromaccountid,fromemployeeid,toaccountid,toemployeeid,rcvddate,amount,details,includeincalc,orgid,createdbyid,createdate)" +
                        " values(:fromAccountID,:fromEmployeeID,:toAccountID,:toEmployeeID,:receivedDate,:amount,:details,:includeInCalc,:orgId,:createdbyId,:createDate)",
                new BeanPropertySqlParameterSource(internalTransferBean));
    }
}
