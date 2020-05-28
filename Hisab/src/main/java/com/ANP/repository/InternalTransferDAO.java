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
public class InternalTransferDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int createInternalTransfer(InternalTransferBean internalTransferBean) {
        return namedParameterJdbcTemplate.update(
                "insert into internaltransfer (fromaccountid,fromemployeeid,toaccountid,toemployeeid,rcvddate,amount,details,includeincalc,orgid,createdbyid,createdate)" +
                        " values(:fromAccountID,:fromEmployeeID,:toAccountID,:toEmployeeID,:receivedDate,:amount,:details,:includeInCalc,:orgId,:createdbyId,:createDate)",
                new BeanPropertySqlParameterSource(internalTransferBean));
    }

    public List<InternalTransferBean> listInternalTransfer(long orgID, List<SearchParam> searchParams, String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);
        return namedParameterJdbcTemplate.query(
                "select e.mobile,e.first,e.last, internal.details," +
                        " (select emp.first from employee emp where emp.id = internal.fromemployeeid) as fromfirst," +
                        " (select emp.last from employee emp where emp.id = internal.fromemployeeid) as fromlast, " +
                        " (select emp.mobile from employee emp where emp.id = internal.fromemployeeid) as frommobile " +
                        "from employee e, internaltransfer internal where e.id=internal.toemployeeid and internal.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new InternalTransferMapper());

    }

    private static final class InternalTransferMapper implements RowMapper<InternalTransferBean> {
        public InternalTransferBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            InternalTransferBean internalTransferBean = new InternalTransferBean();
            internalTransferBean.setDetails(rs.getString("internal.details"));
            internalTransferBean.getFromEmployee().setFirst(rs.getString("fromfirst"));
            internalTransferBean.getFromEmployee().setLast(rs.getString("fromlast"));
            internalTransferBean.getFromEmployee().setMobile(rs.getString("frommobile"));
            internalTransferBean.getToEmployee().setFirst(rs.getString("e.first"));
            internalTransferBean.getToEmployee().setLast(rs.getString("e.last"));
            internalTransferBean.getToEmployee().setMobile(rs.getString("e.mobile"));
            return internalTransferBean;
        }
    }
}
