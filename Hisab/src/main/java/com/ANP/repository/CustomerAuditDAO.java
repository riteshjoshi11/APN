package com.ANP.repository;

import com.ANP.bean.CustomerAuditBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerAuditDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<CustomerAuditBean> listCustomerAudit(long orgID, List<SearchParam> searchParams, String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "custau.id desc";
        }

        return namedParameterJdbcTemplate.query("select custau.* , c.name, c.firmname, c.city " +
                        "from customer c, customeraudit custau where custau.customerid = c.id and custau.orgid=:orgID and (custau.isdeleted is null or  custau.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by "+ orderBy+" limit  :noOfRecordsToShow" + " offset :startIndex",
                         param, new CustomerAuditMapper());
    }

    private static final class CustomerAuditMapper implements RowMapper<CustomerAuditBean> {
        public CustomerAuditBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setAuditId(rs.getLong("custau.id"));
            customerAuditBean.setCustomerid(rs.getString("custau.customerid"));
            customerAuditBean.setAccountid(rs.getLong("custau.accountid"));
            customerAuditBean.setType(rs.getString("custau.type"));
            customerAuditBean.setAmount(rs.getBigDecimal("custau.amount"));
            customerAuditBean.setNewbalance(rs.getBigDecimal("custau.newbalance"));
            customerAuditBean.setOperation(rs.getString("custau.operation"));
            customerAuditBean.setOtherPartyName(rs.getString("custau.otherparty"));
            customerAuditBean.setPreviousbalance(rs.getBigDecimal("custau.previousbalance"));
            customerAuditBean.setTransactionDate(rs.getTimestamp("custau.txndate"));
            customerAuditBean.setCreateDate(rs.getTimestamp("custau.date"));
            customerAuditBean.setOrgId(rs.getLong("custau.orgid"));
            customerAuditBean.getCustomerBean().setName(rs.getString("c.name"));
            customerAuditBean.getCustomerBean().setFirmname(rs.getString("c.firmname"));
            customerAuditBean.getCustomerBean().setCity(rs.getString("c.city"));
            return customerAuditBean;
        }
    }
}