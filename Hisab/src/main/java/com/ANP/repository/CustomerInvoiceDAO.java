package com.ANP.repository;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.PurchaseFromVendorBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerInvoiceDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
        TODO: JOSHI you need to run updated SQL, i have created this method and nothing need to change here.
     */
    public int createInvoice(CustomerInvoiceBean invoiceBean) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO customerinvoice(tocustomerId,date,CGST,orderamount,SGST,IGST,extra,totalamount,orgId,createdById,note,includeInReport,includeincalc,toaccountid,invoiceno) "+
                        " VALUES(:toCustomerId,:date,:CGST,:orderAmount,:SGST,:IGST,:extra,:totalAmount,:orgId,:createdById,:note,:includeInReport,:includeInCalc,:toAccountId,:invoiceNo); ",
                new BeanPropertySqlParameterSource(invoiceBean));
    }

}
