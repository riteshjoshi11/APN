package com.ANP.repository;

import com.ANP.bean.InternalTransferBean;
import com.ANP.bean.PaymentReceivedBean;
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
public class PaymentReceivedDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createPaymentReceived(PaymentReceivedBean paymentReceivedBean) {
        return namedParameterJdbcTemplate.update(
                "insert into paymentreceived (fromaccountid,fromcustomerid,toaccountid,toemployeeid,rcvddate,amount,details,includeincalc,orgid,createdbyid,createdate)" +
                        " values(:fromAccountID,:fromCustomerID,:toAccountID,:toEmployeeID,:receivedDate,:amount,:details,:includeInCalc,:orgId,:createdbyId,:createDate)",
                new BeanPropertySqlParameterSource(paymentReceivedBean));
    }

    public List<PaymentReceivedBean> listPaymentReceivedPaged(long orgID, List<SearchParam> searchParam, String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "id desc";
        }

        return namedParameterJdbcTemplate.query(
                "select c.name, c.city, c.firmname, c.gstin, c.mobile1, c.state, prcvd.id, prcvd.fromaccountid,prcvd.fromcustomerid,prcvd.toaccountid," +
                        "prcvd.toemployeeid,prcvd.rcvddate,prcvd.amount,prcvd.details,prcvd.includeincalc,prcvd.orgid," +
                        "(select emp.first from employee emp where emp.id = prcvd.toemployeeid) as firstName," +
                        "(select emp.last from employee emp where emp.id = prcvd.toemployeeid) as lastName from customer c, paymentreceived " +
                        "prcvd where c.id=prcvd.fromcustomerid and prcvd.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParam) + " order by  "+ orderBy+"  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new PaymentReceivedDAO.PaymentReceivedMapper());
    }

    private static final class PaymentReceivedMapper implements RowMapper<PaymentReceivedBean> {
        public PaymentReceivedBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            PaymentReceivedBean paymentReceivedBean = new PaymentReceivedBean();
            paymentReceivedBean.getCustomerBean().setName(rs.getString("c.name"));
            paymentReceivedBean.getCustomerBean().setCity(rs.getString("c.city"));
            paymentReceivedBean.getCustomerBean().setFirmname(rs.getString("c.firmname"));
            paymentReceivedBean.getCustomerBean().setGstin(rs.getString("c.gstin"));
            paymentReceivedBean.getCustomerBean().setMobile1(rs.getString("c.mobile1"));
            paymentReceivedBean.getCustomerBean().setState(rs.getString("c.state"));

            paymentReceivedBean.setPaymentReceivedID(rs.getLong("prcvd.id"));
            paymentReceivedBean.setFromAccountID(rs.getLong("prcvd.fromaccountid"));
            paymentReceivedBean.setFromCustomerID(rs.getString("prcvd.fromcustomerid"));

            paymentReceivedBean.setToAccountID(rs.getLong("prcvd.toaccountid"));
            paymentReceivedBean.setToEmployeeID(rs.getString("prcvd.toemployeeid"));
            paymentReceivedBean.setReceivedDate(rs.getDate("prcvd.rcvddate"));
            paymentReceivedBean.setAmount(rs.getFloat("prcvd.amount"));
            paymentReceivedBean.setDetails(rs.getString("prcvd.details"));
            paymentReceivedBean.setOrgId(rs.getLong("prcvd.orgid"));
            paymentReceivedBean.setIncludeInCalc(rs.getBoolean("prcvd.includeincalc"));
            paymentReceivedBean.getEmployeeBean().setFirst(rs.getString("firstName"));
            paymentReceivedBean.getEmployeeBean().setLast(rs.getString("lastName"));
            return paymentReceivedBean;
        }
    }
}
