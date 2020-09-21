package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class CustomerDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int createCustomer(CustomerBean customerBean) {
        System.out.println("customer " + customerBean.getName());
        try {
            String idSql = "SELECT getcustomerId() ";
            Map param = new HashMap<String, Object>();
            String customerId = namedParameterJdbcTemplate.queryForObject(idSql, param, String.class);
            customerBean.setCustomerID(customerId);

            System.out.println("customer id " + customerId);
            String sql = "insert into customer (id,name,city,state,gstin,transporter,mobile1,mobile2,firmname,billingadress,orgid,createdbyid) " +
                    "values(:customerID,:name,:city,:gstin,:state,:transporter,:mobile1,:mobile2,:firmname,:billingadress,:orgId,:createdbyId)";
            int updated = namedParameterJdbcTemplate.update(sql
                    , new BeanPropertySqlParameterSource(customerBean));
            return updated;
        }
        catch (DuplicateKeyException e) {
            throw new CustomAppException("Duplicate Entry","SERVER.CREATE_CUSTOMER.DUPLICATE", HttpStatus.EXPECTATION_FAILED);
        }
    }

    private static final class CustomerMapper implements RowMapper<CustomerBean> {
        public CustomerBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerBean cus = new CustomerBean();
            cus.setCustomerID(rs.getString("id"));
            cus.setName(rs.getString("name"));
            cus.setCity(rs.getString("city"));
            return cus;
        }
    }

    /*
     * This method is used during the login process if a user is being login as customer
     */
    public CustomerBean getCustomerUsingMobile1(String mobile) {
        CustomerBean customerBean = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mobileNumber", mobile);

        List<CustomerBean> customerBeanList = namedParameterJdbcTemplate.query("select * from customer where mobile1=:mobileNumber",
                param, new CustomerMapper());
        if (customerBeanList != null && customerBeanList.size() > 0) {
            customerBean = customerBeanList.get(0);
        }
        return customerBean;
    }

    /*
     * This is one of the important method for the UI to list customer and vendor with their account balance
     * 1. Join Customer and Account Table on Customer.CustomerId=Account.OwnerID
     * 2. orgId is mandatory AND Condition
     * 3. Add other search parameters as those are coming as part of search
     * 4. searchParam is generic implementation
     * Please note - this method supports pagination
     * order By (asc,desc) will be added
     */
    public List<CustomerBean> listCustomerANDVendorWithBalancePaged(long orgID, Collection<SearchParam> searchParams,
                                                                    String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow",noOfRecordsToShow);
        param.put("startIndex",startIndex-1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = " account.currentbalance asc ";
        }

        return namedParameterJdbcTemplate.query("select customer.*, account.currentbalance, account.id " +
                         " from customer,account where customer.id=account.ownerid and customer.orgid=:orgID " +
                           ANPUtils.getWhereClause(searchParams) + " order by  "+ orderBy+"  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                           param, new FullCustomerMapper()) ;
    }


    private static final class FullCustomerMapper implements RowMapper<CustomerBean> {
        public CustomerBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerBean cus = new CustomerBean();
            cus.setCustomerID(rs.getString("customer.id"));
            cus.setName(rs.getString("customer.name"));
            cus.setCity(rs.getString("customer.city"));
            cus.setGstin(rs.getString("customer.gstin"));
            cus.setTransporter(rs.getString("customer.transporter"));
            cus.setMobile1(rs.getString("customer.mobile1"));
            cus.setMobile2(rs.getString("customer.mobile2"));
            cus.setFirmname(rs.getString("customer.firmname"));
            cus.setBillingadress(rs.getString("customer.billingadress"));
            cus.setOrgId(rs.getLong("customer.orgid"));
            cus.setState(rs.getString("customer.state"));
            cus.setSendPaymentReminders(rs.getBoolean("customer.sendPaymentReminders"));
            cus.setAccountBalance(rs.getFloat("account.currentbalance"));
            cus.setCreateDate(rs.getTimestamp("customer.createdate"));
            cus.setCreatedbyId(rs.getString("customer.createdbyid"));
            cus.setAccountId(rs.getLong("account.id"));
            return cus;
        }
    }
    public int updateCustomer(CustomerBean customerBean){
        return namedParameterJdbcTemplate.update("update customer set name = :name," +
                "city=:city, gstin=:gstin , transporter = :transporter , mobile2 = :mobile2 " +
                ", firmname = :firmname , billingadress = :billingadress , createdbyid = :createdbyId , sendPaymentReminders = :sendPaymentReminders," +
                " state = :state where orgid = :orgId and id = :customerID",new BeanPropertySqlParameterSource(customerBean));
    }

    public CustomerBean getCustomerById(Long orgId, String customerId) {
        List<SearchParam> searchParams = new ArrayList<>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("customer.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue(customerId);
        searchParams.add(param);
        List<CustomerBean> customerBeanList = listCustomerANDVendorWithBalancePaged(orgId, searchParams,"",1,1);
        if (customerBeanList!=null && !customerBeanList.isEmpty()) {
            return customerBeanList.get(0);
        }
        return null;
    }

    public int updateSendPaymentReminders(CustomerBean customerBean) {

        Map<String,Object> param = new HashMap<>();
        param.put("orgid",customerBean.getOrgId());
        param.put("customerid",customerBean.getCustomerID());
        param.put("sendpaymentreminders",customerBean.isSendPaymentReminders());


        return namedParameterJdbcTemplate.update("update customer set " +
                "sendpaymentreminders = :sendpaymentreminders where orgid = :orgid and id = :customerid",param);

    }

}