package com.ANP.repository;


import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.InternalTransferBean;
import com.ANP.bean.RetailSale;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class RetailSaleDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createRetailSale(RetailSale retailSale) {
        if(!retailSale.isForceCreate()) {
            isDuplicateSuspect(retailSale);
        }
        return namedParameterJdbcTemplate.update(
                "INSERT INTO retailsale(date,amount,orgid,createdbyid,notes,includeincalc,fromaccountid,fromemployeeid) " +
                        " VALUES(:date,:amount,:orgId,:createdbyId,:notes,:includeincalc,:fromaccountid,:fromemployeeid); ",
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

        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "retail.id desc";
        }

        return namedParameterJdbcTemplate.query(
                "select e.mobile,e.first,e.last, retail.id, retail.amount, retail.orgid," +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee where employee.id = retail.createdbyid) as createdByEmployeeName, " +
                        "retail.fromaccountid, retail.fromemployeeid,retail.date,retail.notes, retail.includeincalc,retail.createdate, retail.createdbyid " +
                       " from employee e, retailsale retail where e.id=retail.fromemployeeid and retail.orgid=:orgID " +
                        " and (retail.isdeleted is null or retail.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  "+ orderBy+"  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new RetailEntryMapper());

    }

    public static final class RetailEntryMapper implements RowMapper<RetailSale>
    {
        public RetailSale mapRow (ResultSet rs, int rowNum) throws SQLException
        {
            RetailSale ret = new RetailSale();
            ret.setRetailSaleId(rs.getLong("retail.id"));
            ret.setAmount(rs.getLong("retail.amount"));
            ret.setDate(rs.getTimestamp("retail.date"));
            ret.setIncludeincalc(rs.getBoolean("retail.includeincalc"));
            ret.setFromaccountid(rs.getInt("retail.fromaccountid"));
            ret.setFromemployeeid(rs.getString("retail.fromemployeeid"));
            ret.setNotes(rs.getString("retail.notes"));
            ret.setOrgId(rs.getLong("retail.orgid"));
            ret.getFromEmployee().setFirst(rs.getString("e.first"));
            ret.getFromEmployee().setLast(rs.getString("e.last"));
            ret.getFromEmployee().setMobile(rs.getString("e.mobile"));
            ret.setCreateDate(rs.getTimestamp("retail.createdate"));
            ret.setCreatedbyId(rs.getString("retail.createdbyid"));
            ret.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));
            return ret;
        }
    }

    /*
      //Check for duplicate entry here programmatically
      //if found then throw an error

     */
    public void isDuplicateSuspect(RetailSale retailSale){
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String,Object> params = new HashMap<>();
        params.put("date", retailSale.getDate());
        params.put("orgid", retailSale.getOrgId());

        long actualamount = (long)(retailSale.getAmount());
        params.put("amount", actualamount);


        Integer count = namedParameterJdbcTemplate.queryForObject(   "select count(*) from ( SELECT  floor(amount) " +
                " as amount ,id FROM retailsale where orgid=:orgid and date=:date and (isdeleted is null or isdeleted <> true) " +
                "  order by id desc limit 1) retailsale where amount = :amount",params, Integer.class);

        System.out.println(count);
        if(count>0) {
            throw new CustomAppException("The Retail Sale looks like duplicate",
                    "SERVER.CREATE_RETAILSALE.DUPLICATE_SUSPECTSERVER.CREATE_RETAILSALE.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }
    public int updateRetailSale(RetailSale retailSale){
        return namedParameterJdbcTemplate.update("update retailsale set notes = :notes where orgid = :orgId and " +
                "retailsale.id = :retailSaleId", new BeanPropertySqlParameterSource(retailSale));
    }
    public RetailSale getRetailSaleById(Long orgId, Long retailSaleId) {
        java.util.List<SearchParam> searchParams = new ArrayList<SearchParam>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("retail.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue("" + retailSaleId);
        searchParams.add(param);
        List<RetailSale> retailSaleList = listRetailEntryPaged(orgId, searchParams, "", 1, 1);
        if (retailSaleList != null && !retailSaleList.isEmpty()) {
            return retailSaleList.get(0);
        }
        throw new CustomAppException("Retail Looks to be empty", "SERVER.RETAIL.NOT_EXISTING", HttpStatus.CONFLICT);
    }
}