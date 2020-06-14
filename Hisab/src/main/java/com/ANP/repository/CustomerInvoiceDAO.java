package com.ANP.repository;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.RetailSale;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class CustomerInvoiceDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
        TODO: JOSHI you need to run updated SQL, i have created this method and nothing need to change here.
     */
    public int createInvoice(CustomerInvoiceBean invoiceBean) {
        System.out.println(" CreateInvoice: invoiceBean.forceCreate[" + invoiceBean.isForceCreate() + "]");
        if(!invoiceBean.isForceCreate()) {
            isDuplicateSuspect(invoiceBean);
        }

        return namedParameterJdbcTemplate.update(
                "INSERT INTO customerinvoice(tocustomerId,date,CGST,orderamount,SGST,IGST,extra,totalamount,orgId,createdById,note,includeInReport,includeincalc,toaccountid,invoiceno) " +
                        " VALUES(:toCustomerId,:date,:CGST,:orderAmount,:SGST,:IGST,:extra,:totalAmount,:orgId,:createdbyId,:note,:includeInReport,:includeInCalc,:toAccountId,:invoiceNo); ",
                new BeanPropertySqlParameterSource(invoiceBean));
    }

    public List<CustomerInvoiceBean> listSalesPaged(long orgID, List<SearchParam> searchParams,
                                                    String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "id desc";
        }

        return namedParameterJdbcTemplate.query(
                "select cusinv.id,cusinv.tocustomerid,cusinv.date,cusinv.orderamount,cusinv.cgst,cusinv.sgst,cusinv.igst," +
                        "cusinv.totalamount,cusinv.invoiceno,cusinv.toaccountid,cusinv.orgid,cusinv.includeinreport," +
                        "cusinv.includeincalc,c.state, c.name,c.firmname,c.city,c.mobile1,c.gstin from customer c," +
                        " customerinvoice cusinv where c.id=cusinv.tocustomerid and cusinv.orgid=:orgID " +
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
//          customerInvoiceBean.getCustomerBean().setMobile2(rs.getString("c.mobile2"));
            customerInvoiceBean.setOrderAmount(rs.getDouble("orderamount"));
            customerInvoiceBean.setCGST(rs.getDouble("cusinv.cgst"));
            customerInvoiceBean.setIGST(rs.getFloat("cusinv.igst"));
            customerInvoiceBean.setSGST(rs.getFloat("cusinv.sgst"));
            customerInvoiceBean.setTotalAmount(rs.getFloat("cusinv.totalamount"));
            customerInvoiceBean.setInvoiceNo(rs.getString("cusinv.invoiceno"));
            customerInvoiceBean.setOrgId(rs.getLong("cusinv.orgid"));
            customerInvoiceBean.setToAccountId(rs.getLong("cusinv.toaccountid"));
            customerInvoiceBean.setIncludeInCalc(rs.getBoolean("cusinv.includeincalc"));
            customerInvoiceBean.setIncludeInReport(rs.getBoolean("cusinv.includeinreport"));
            customerInvoiceBean.setDate(rs.getDate("cusinv.date"));
            return customerInvoiceBean;
        }
    }

    public void isDuplicateSuspect(CustomerInvoiceBean customerInvoiceBean) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        System.out.println("isDuplicate Suspect...");
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", customerInvoiceBean.getOrgId());
        params.put("tocustomerid", customerInvoiceBean.getToCustomerId());

        long actualamount = (long)(customerInvoiceBean.getOrderAmount());
        params.put("amount", actualamount);

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( select  floor(orderamount) as orderamount ," +
                "id from customerinvoice where orgid=:orgid and tocustomerid=:tocustomerid" +
                "  order by id desc limit 1) sale where orderamount = :amount",params, Integer.class);

        System.out.println("count =" + count);
        if (count > 0) {
            throw new CustomAppException("The Sales looks like duplicate", "SERVER.CREATE_SALE.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }
}
