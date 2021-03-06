package com.ANP.repository;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Repository
public class CustomerInvoiceDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CustomerInvoiceDAO.class);

    public int createInvoice(CustomerInvoiceBean invoiceBean) {
        logger.trace(" CreateInvoice: invoiceBean.forceCreate[" + invoiceBean.isForceCreate() + "] date[" + invoiceBean.getDate() + "]");
        if(!invoiceBean.isForceCreate()) {
            isDuplicateSuspect(invoiceBean);
        }

        return namedParameterJdbcTemplate.update(
                "INSERT INTO customerinvoice(tocustomerId,date,CGST,orderamount,SGST,IGST,extra,totalamount,orgId,createdById,note,includeInReport,includeincalc,toaccountid,invoiceno) " +
                        " VALUES(:toCustomerId,:date,:CGST,:orderAmount,:SGST,:IGST,:extra,:totalAmount,:orgId,:createdbyId,:note,:includeInReport,:includeInCalc,:toAccountId,:invoiceNo); ",
                new BeanPropertySqlParameterSource(invoiceBean));
    }

    public java.util.List<CustomerInvoiceBean> listSalesPaged(long orgID, java.util.List<com.ANP.bean.SearchParam> searchParams,
                                                    String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);

        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "cusinv.id desc";
        }

        return namedParameterJdbcTemplate.query(
                "select cusinv.id,cusinv.tocustomerid,cusinv.date,cusinv.orderamount,cusinv.cgst,cusinv.sgst,cusinv.igst," +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee e where e.id = cusinv.createdbyid) as createdByEmployeeName, " +
                        "cusinv.totalamount,cusinv.invoiceno,cusinv.toaccountid,cusinv.orgid,cusinv.includeinreport," +
                        "cusinv.includeincalc,cusinv.createdate,cusinv.createdbyid,c.state, c.name,c.firmname,c.city,c.mobile1,c.gstin,cusinv.note from customer c," +
                        " customerinvoice cusinv where c.id=cusinv.tocustomerid and cusinv.orgid=:orgID and (cusinv.isdeleted is null or cusinv.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new SalesPagedMapper());
    }

    private static final class SalesPagedMapper implements RowMapper<CustomerInvoiceBean> {
        public CustomerInvoiceBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerInvoiceBean customerInvoiceBean = new CustomerInvoiceBean();

            customerInvoiceBean.getCustomerBean().setName(rs.getString("c.name"));
            customerInvoiceBean.getCustomerBean().setCity(rs.getString("c.city"));
            customerInvoiceBean.getCustomerBean().setFirmname(rs.getString("c.firmname"));
            customerInvoiceBean.getCustomerBean().setGstin(rs.getString("c.gstin"));
            customerInvoiceBean.getCustomerBean().setMobile1(rs.getString("c.mobile1"));
            customerInvoiceBean.getCustomerBean().setState(rs.getString("c.state"));
            customerInvoiceBean.setInvoiceID(rs.getLong("cusinv.id"));
            customerInvoiceBean.setOrderAmount(rs.getBigDecimal("orderamount"));
            customerInvoiceBean.setCGST(rs.getBigDecimal("cusinv.cgst"));
            customerInvoiceBean.setIGST(rs.getBigDecimal("cusinv.igst"));
            customerInvoiceBean.setSGST(rs.getBigDecimal("cusinv.sgst"));
            customerInvoiceBean.setTotalAmount(rs.getBigDecimal("cusinv.totalamount"));
            customerInvoiceBean.setInvoiceNo(rs.getString("cusinv.invoiceno"));
            customerInvoiceBean.setOrgId(rs.getLong("cusinv.orgid"));
            customerInvoiceBean.setToAccountId(rs.getLong("cusinv.toaccountid"));
            customerInvoiceBean.setToCustomerId(rs.getString("cusinv.tocustomerid"));
            customerInvoiceBean.setIncludeInCalc(rs.getBoolean("cusinv.includeincalc"));
            customerInvoiceBean.setIncludeInReport(rs.getBoolean("cusinv.includeinreport"));
            customerInvoiceBean.setDate(rs.getTimestamp("cusinv.date"));
            customerInvoiceBean.setCreateDate(rs.getTimestamp("cusinv.createdate"));
            customerInvoiceBean.setCreatedbyId(rs.getString("cusinv.createdbyid"));
            customerInvoiceBean.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));
            customerInvoiceBean.setNote(rs.getString("cusinv.note"));
            return customerInvoiceBean;
        }
    }

    private void isDuplicateSuspect(CustomerInvoiceBean customerInvoiceBean) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        logger.trace("isDuplicate Suspect...");
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", customerInvoiceBean.getOrgId());
        params.put("tocustomerid", customerInvoiceBean.getToCustomerId());

        params.put("amount", customerInvoiceBean.getTotalAmount().longValue());

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( select  floor(totalamount) as totalamount ," +
                "id from customerinvoice where orgid=:orgid and tocustomerid=:tocustomerid and (isdeleted is null or isdeleted <> true) " +
                "  order by id desc limit 1) sale where totalamount = :amount",params, Integer.class);

        logger.trace("count =" + count);
        if (count > 0) {
            throw new CustomAppException("The Sales looks like duplicate", "SERVER.CREATE_SALE.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }


    /*
     * @TODO: Paras please include notes/details field as well.
     * also there is big mistake here the current logic will update all the Sales for an organization.
     * Always include primary key for the update.
     */
    public int updateSales(CustomerInvoiceBean customerInvoiceBean){
        logger.trace("customerInvoiceBean.invoiceNo[" + customerInvoiceBean.getInvoiceNo() + "]");
        return namedParameterJdbcTemplate.update("update customerinvoice set cgst = :CGST," +
                 " sgst=:SGST, igst=:IGST, note = :note,date=:date,invoiceno=:invoiceNo, orderamount=:orderAmount, includeinreport=:includeInReport  " +
                "where orgid = :orgId and id = :invoiceID",new BeanPropertySqlParameterSource(customerInvoiceBean));
    }

    /*
    API invoked by UI before UpdateSales
     */
    public CustomerInvoiceBean getSalesById(Long orgId, Long salesID) {
        java.util.List<SearchParam> searchParams = new ArrayList<SearchParam>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("cusinv.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue("" + salesID);
        searchParams.add(param);
        List<CustomerInvoiceBean> expenseList = listSalesPaged(orgId, searchParams, "", 1, 1);
        if (expenseList != null && !expenseList.isEmpty()) {
            return expenseList.get(0);
        }
        throw new CustomAppException("Sale Looks to be empty", "SERVER.SALE.NOT_EXISTING", HttpStatus.CONFLICT);
    }
}
