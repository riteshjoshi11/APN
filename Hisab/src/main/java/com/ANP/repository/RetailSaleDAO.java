package com.ANP.repository;

import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.RetailSale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RetailSaleDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createRetailSale(RetailSale retailSale) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO retailsale(date,amount,orgid,createdbyid,notes,includeincalc,fromaccountid,fromemployeeid,createdate) " +
                        " VALUES(:date,:amount,:orgId,:createdbyId,:notes,:includeincalc,:fromaccountid,:fromemployeeid,:createDate); ",
                new BeanPropertySqlParameterSource(retailSale));
    }
}
