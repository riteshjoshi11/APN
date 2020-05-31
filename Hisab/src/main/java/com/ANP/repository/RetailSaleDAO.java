package com.ANP.repository;


import com.ANP.bean.RetailSale;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public List<RetailSale> listRetailEntryPaged(long orgID, List<SearchParam> searchParams, String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);
        return namedParameterJdbcTemplate.query(
                "select e.mobile,e.first,e.last, retail.amount, retail.orgid," +
                        "retail.fromaccountid, retail.fromemployeeid,retail.date,retail.notes, retail.includeincalc" +
                       " from employee e, retailsale retail where e.id=retail.fromemployeeid and retail.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new RetailEntryMapper());

    }

    public static final class RetailEntryMapper implements RowMapper<RetailSale>
    {
        public RetailSale mapRow (ResultSet rs, int rowNum) throws SQLException
        {
            RetailSale ret = new RetailSale();
            ret.setAmount(rs.getLong("retail.amount"));
            ret.setDate(rs.getDate("retail.date"));
            ret.setIncludeincalc(rs.getBoolean("retail.includeincalc"));
            ret.setFromaccountid(rs.getInt("retail.fromaccountid"));
            ret.setFromemployeeid(rs.getString("retail.fromemployeeid"));
            ret.setNotes(rs.getString("retail.notes"));
            ret.setOrgId(rs.getLong("retail.orgid"));
            ret.getFromEmployee().setFirst(rs.getString("e.first"));
            ret.getFromEmployee().setLast(rs.getString("e.last"));
            ret.getFromEmployee().setMobile(rs.getString("e.mobile"));
            return ret;
        }
    }
}