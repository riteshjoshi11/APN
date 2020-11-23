package com.ANP.repository;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;


@Repository
public class PurchaseFromVendorDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /*
     *   TODO: Joshi i have corrected this method, you need to run the latest 29April version of DB patch
     *  No need to change anything here.
     */
    @Transactional(rollbackFor = Exception.class)
    public int createBill(PurchaseFromVendorBean purchaseFromVendorBean) {
        if (!purchaseFromVendorBean.isForceCreate()) {
            isDuplicateSuspect(purchaseFromVendorBean);
        }
        return namedParameterJdbcTemplate.update(
                "INSERT INTO purchasefromvendor(fromcustomerId,date,CGST,orderamount,SGST,IGST,extra,totalamount,orgId,createdById,note,includeInReport,includeincalc,fromaccountid,billno) " +
                        " VALUES(:fromCustomerId,:date,:CGST,:orderAmount,:SGST,:IGST,:extra,:totalAmount,:orgId,:createdbyId,:note,:includeInReport,:includeInCalc,:fromAccountId,:billNo); ",
                new BeanPropertySqlParameterSource(purchaseFromVendorBean));
    }


    public List<PurchaseFromVendorBean> listPurchasesPaged(long orgID, Collection<SearchParam> searchParams,
                                                           String orderBy, int noOfRecordsToShow, int startIndex) {

        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);


        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "p.id desc";
        }

        return namedParameterJdbcTemplate.query("select customer.id,customer.name, customer.city," +
                        "customer.gstin,customer.mobile1,customer.firmname, customer.orgid, customer.state, " +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee where employee.id = p.createdbyid) as createdByEmployeeName, " +
                        "p.id, p.fromaccountid, p.date,p.CGST,p.orderamount,p.SGST," +
                        "p.IGST,p.extra,p.totalamount,p.note,p.includeInReport," +
                        "p.includeincalc,p.billno,p.createdate,p.createdbyid " +
                        " from customer,purchasefromvendor p where p.orgid=:orgId and customer.id=p.fromcustomerid and " +
                        " (p.isdeleted is null or p.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by " + orderBy + " limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullPurchaseFromVendorMapper());

    }

    private static final class FullPurchaseFromVendorMapper implements RowMapper<PurchaseFromVendorBean> {
        public PurchaseFromVendorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            PurchaseFromVendorBean purchaseFromVendorBean = new PurchaseFromVendorBean();
            purchaseFromVendorBean.getCustomerBean().setCustomerID(rs.getString("customer.id"));
            purchaseFromVendorBean.getCustomerBean().setName(rs.getString("customer.name"));
            purchaseFromVendorBean.getCustomerBean().setCity(rs.getString("customer.city"));
            purchaseFromVendorBean.getCustomerBean().setGstin(rs.getString("customer.gstin"));
            purchaseFromVendorBean.getCustomerBean().setMobile1(rs.getString("customer.mobile1"));
            purchaseFromVendorBean.getCustomerBean().setFirmname(rs.getString("customer.firmname"));
            purchaseFromVendorBean.getCustomerBean().setOrgId(rs.getLong("customer.orgid"));
            purchaseFromVendorBean.getCustomerBean().setState(rs.getString("customer.state"));

            purchaseFromVendorBean.setOrgId(rs.getLong("customer.orgid"));
            purchaseFromVendorBean.setPurchaseID(rs.getLong("p.id"));
            purchaseFromVendorBean.setDate(rs.getTimestamp("p.date"));
            purchaseFromVendorBean.setCGST(rs.getBigDecimal("p.CGST"));
            purchaseFromVendorBean.setSGST(rs.getBigDecimal("p.SGST"));
            purchaseFromVendorBean.setIGST(rs.getBigDecimal("p.IGST"));
            purchaseFromVendorBean.setExtra(rs.getBigDecimal("p.extra"));
            purchaseFromVendorBean.setTotalAmount(rs.getBigDecimal("p.totalamount"));
            purchaseFromVendorBean.setNote(rs.getString("p.note"));
            purchaseFromVendorBean.setIncludeInReport(rs.getBoolean("p.includeInReport"));
            purchaseFromVendorBean.setIncludeInCalc(rs.getBoolean("p.includeincalc"));
            purchaseFromVendorBean.setBillNo(rs.getString("p.billno"));
            purchaseFromVendorBean.setCreateDate(rs.getTimestamp("p.createdate"));
            purchaseFromVendorBean.setCreatedbyId(rs.getString("p.createdbyid"));
            purchaseFromVendorBean.setFromAccountId(rs.getLong("p.fromaccountid"));
            purchaseFromVendorBean.setFromCustomerId(rs.getString("customer.id"));
            purchaseFromVendorBean.setOrderAmount(rs.getBigDecimal("p.orderamount"));
            purchaseFromVendorBean.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));
            return purchaseFromVendorBean;
        }
    }

    public void isDuplicateSuspect(PurchaseFromVendorBean purchaseFromVendorBean) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", purchaseFromVendorBean.getOrgId());
        params.put("fromcustomerid", purchaseFromVendorBean.getFromCustomerId());

        params.put("amount", purchaseFromVendorBean.getTotalAmount().longValue());

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( SELECT  floor(totalamount) as totalamount " +
                ",id FROM purchasefromvendor where orgid=:orgid and fromcustomerid=:fromcustomerid and (isdeleted is null or isdeleted <> true) " +
                "  order by id desc limit 1) purchase where totalamount = :amount", params, Integer.class);
        System.out.println(count);
        if (count > 0) {
            throw new CustomAppException("The purchase from vendor looks like duplicate", "SERVER.CREATE_PURCHASE_ENTRY.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }

    /*
     * @TODO: Paras please include notes/details field as well.
     * also there is big mistake here the current logic will update all the Purchase for an organization.
     * Always include primary key for the update.
     */
    @Transactional(rollbackFor = Exception.class)
    public int updatePurchase(PurchaseFromVendorBean purchaseFromVendorBean) {
        return namedParameterJdbcTemplate.update("update purchasefromvendor set cgst = :CGST," +
                "sgst=:SGST, igst=:IGST, note = :note,date=:date,billno=:billNo,orderamount=:orderAmount, includeinreport=:includeInReport" +
                " where orgid = :orgId and id = :purchaseID", new BeanPropertySqlParameterSource(purchaseFromVendorBean));
    }


     /*
      * API invoked by UI before UpdateSales
     */
    public PurchaseFromVendorBean getPurchaseById(Long orgId, Long purchaseId) {
        java.util.List<SearchParam> searchParams = new ArrayList<SearchParam>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("p.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue("" + purchaseId);
        searchParams.add(param);
        List<PurchaseFromVendorBean> purchaseFromVendorBeans = listPurchasesPaged(orgId, searchParams, "", 1, 1);
        if (purchaseFromVendorBeans != null && !purchaseFromVendorBeans.isEmpty()) {
            return purchaseFromVendorBeans.get(0);
        }

        throw new CustomAppException("SERVER.PURCHASEFROMVENDOR.GET_PURCHASE_ID","Purchase record could not be found for given ID", HttpStatus.EXPECTATION_FAILED);
    }


}