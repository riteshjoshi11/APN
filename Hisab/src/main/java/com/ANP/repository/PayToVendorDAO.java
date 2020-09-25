package com.ANP.repository;

import com.ANP.bean.InternalTransferBean;
import com.ANP.bean.PayToVendorBean;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PayToVendorDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createPayToVendor(PayToVendorBean payToVendorBean) {
        if(!payToVendorBean.isForceCreate()) {
            isDuplicateSuspect(payToVendorBean);
        }

        return namedParameterJdbcTemplate.update(
                "insert into paytovendor(fromaccountid,fromemployeeid,toaccountid,tocustomerid,date,amount,details,includeincalc,orgid,createdbyid)" +
                        " values(:fromAccountID,:fromEmployeeID,:toAccountID,:toCustomerID,:paymentDate,:amount,:details,:includeInCalc,:orgId,:createdbyId)",
                new BeanPropertySqlParameterSource(payToVendorBean));


    }
    public List<PayToVendorBean> listPayToVendorPaged(long orgID, List<SearchParam> searchParams,
                                                      String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "paytov.id desc";
        }
        return namedParameterJdbcTemplate.query(
                "select paytov.id, paytov.fromaccountid, paytov.toaccountid, paytov.date, paytov.amount," +
                     " paytov.details, paytov.fromemployeeid, paytov.tocustomerid, paytov.orgid, paytov.includeincalc," +
                     " paytov.createdate,paytov.createdbyid,c.name,c.firmname,c.city,c.mobile1,c.gstin,c.state, e.first,e.last,e.mobile from customer c, employee e," +
                     " paytovendor paytov where c.id=paytov.tocustomerid and e.id = paytov.fromemployeeid and paytov.orgid=:orgID " +
                     " and (paytov.isdeleted is null or paytov.isdeleted <> true) " +
                      ANPUtils.getWhereClause(searchParams) + " order by  "+ orderBy+"  limit  :noOfRecordsToShow" +
                        " offset :startIndex",
                param, new PayToVendorMapper());
    }

    private static final class PayToVendorMapper implements RowMapper<PayToVendorBean> {
        public PayToVendorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            PayToVendorBean payToVendorBean = new PayToVendorBean();
            payToVendorBean.setPayToVendorID(rs.getLong("paytov.id"));
            payToVendorBean.getCustomerBean().setName(rs.getString("c.name"));
            payToVendorBean.getCustomerBean().setCity(rs.getString("c.city"));
            payToVendorBean.getCustomerBean().setFirmname(rs.getString("c.firmname"));
            payToVendorBean.getCustomerBean().setGstin(rs.getString("c.gstin"));
            payToVendorBean.getCustomerBean().setMobile1(rs.getString("c.mobile1"));
            payToVendorBean.getCustomerBean().setState(rs.getString("c.state"));
            payToVendorBean.setFromAccountID(rs.getLong("paytov.fromaccountid"));
            payToVendorBean.setToAccountID(rs.getLong("paytov.toaccountid"));
            payToVendorBean.setPaymentDate(rs.getTimestamp("paytov.date"));
            payToVendorBean.setAmount(rs.getFloat("paytov.amount"));
            payToVendorBean.setDetails(rs.getString("paytov.details"));
            payToVendorBean.setFromEmployeeID(rs.getString("paytov.fromemployeeid"));
            payToVendorBean.setOrgId(rs.getLong("paytov.orgid"));
            payToVendorBean.setToCustomerID(rs.getString("paytov.tocustomerid"));
            payToVendorBean.setIncludeInCalc(rs.getBoolean("paytov.includeincalc"));
            payToVendorBean.setCreateDate(rs.getTimestamp("paytov.createdate"));
            payToVendorBean.setCreatedbyId(rs.getString("paytov.createdbyid"));

            payToVendorBean.getEmployeeBean().setFirst(rs.getString("e.first"));
            payToVendorBean.getEmployeeBean().setLast(rs.getString("e.last"));
            payToVendorBean.getEmployeeBean().setMobile(rs.getString("e.mobile"));

            return payToVendorBean;
        }
    }

    public void isDuplicateSuspect(PayToVendorBean payToVendorBean){
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String,Object> params = new HashMap<>();
        params.put("orgid", payToVendorBean.getOrgId());
        params.put("tocustomerid", payToVendorBean.getToCustomerID());

        long actualamount = (long)(payToVendorBean.getAmount());
        params.put("amount", actualamount);
        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( SELECT  floor(amount) as " +
                " amount ,id from paytovendor where orgid=:orgid and tocustomerid=:tocustomerid and (isdeleted is null or isdeleted <> true) " +
                " order by id desc limit 1) pay where amount = :amount",params, Integer.class);
        System.out.println(count);
        if(count>0) {
            throw new CustomAppException("The pay to vendor looks like duplicate", "SERVER.CREATE_PAY_TO_VENDOR.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }

    public int updatePayToVendor(PayToVendorBean payToVendorBean) {
        return namedParameterJdbcTemplate.update("update paytovendor set details = :details where orgid = :orgId and paytovendor.id = :payToVendorID"
                ,new BeanPropertySqlParameterSource(payToVendorBean));
    }

    public PayToVendorBean getPayToVendorById(Long orgId, Long payToVendorId) {
        java.util.List<SearchParam> searchParams = new ArrayList<SearchParam>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("paytovendor.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue("" + payToVendorId);
        searchParams.add(param);
        List<PayToVendorBean> payToVendorBeanList = listPayToVendorPaged(orgId, searchParams, "", 1, 1);
        if (payToVendorBeanList != null && !payToVendorBeanList.isEmpty()) {
            return payToVendorBeanList.get(0);
        }
        return null;
    }
}
