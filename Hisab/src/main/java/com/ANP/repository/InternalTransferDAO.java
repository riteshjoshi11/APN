package com.ANP.repository;

import com.ANP.bean.InternalTransferBean;
import com.ANP.bean.PaymentReceivedBean;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InternalTransferDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    public int createInternalTransfer(InternalTransferBean internalTransferBean) {
        if (!internalTransferBean.isForceCreate()) {
            isDuplicateSuspect(internalTransferBean);
        }
        return namedParameterJdbcTemplate.update(
                "insert into internaltransfer (fromaccountid,fromemployeeid,toaccountid,toemployeeid,rcvddate,amount,details,includeincalc,orgid,createdbyid)" +
                        " values(:fromAccountID,:fromEmployeeID,:toAccountID,:toEmployeeID,:receivedDate,:amount,:details,:includeInCalc,:orgId,:createdbyId)",
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
        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "internal.id desc";
        }


        return namedParameterJdbcTemplate.query("select internal.id,internal.orgid, internal.fromemployeeid, internal.fromaccountid, " +
                        "internal.toemployeeid,internal.toaccountid, internal.includeincalc, e.mobile,e.first,e.last, internal.details," +
                        "internal.createdate,internal.createdbyid, internal.rcvddate,internal.amount," +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee where employee.id = internal.createdbyid) as createdByEmployeeName, " +
                        " (select emp.first from employee emp where emp.id = internal.fromemployeeid) as fromfirst," +
                        " (select emp.last from employee emp where emp.id = internal.fromemployeeid) as fromlast, " +
                        " (select emp.mobile from employee emp where emp.id = internal.fromemployeeid) as frommobile " +
                        "from employee e, internaltransfer internal where e.id=internal.toemployeeid and internal.orgid=:orgID " +
                        " and (internal.isdeleted is null or  internal.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow" + " offset :startIndex",
                param, new InternalTransferMapper());
    }

    private static final class InternalTransferMapper implements RowMapper<InternalTransferBean> {
        public InternalTransferBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            InternalTransferBean internalTransferBean = new InternalTransferBean();
            internalTransferBean.setInternalTransferID(rs.getLong("internal.id"));
            internalTransferBean.setOrgId(rs.getLong("internal.orgid"));
            internalTransferBean.setDetails(rs.getString("internal.details"));
            internalTransferBean.getFromEmployee().setFirst(rs.getString("fromfirst"));
            internalTransferBean.getFromEmployee().setLast(rs.getString("fromlast"));
            internalTransferBean.getFromEmployee().setMobile(rs.getString("frommobile"));
            internalTransferBean.getToEmployee().setFirst(rs.getString("e.first"));
            internalTransferBean.getToEmployee().setLast(rs.getString("e.last"));
            internalTransferBean.getToEmployee().setMobile(rs.getString("e.mobile"));
            internalTransferBean.setAmount(rs.getBigDecimal("internal.amount"));
            internalTransferBean.setReceivedDate(rs.getTimestamp("internal.rcvddate"));
            internalTransferBean.setCreateDate(rs.getTimestamp("internal.createdate"));
            internalTransferBean.setCreatedbyId(rs.getString("internal.createdbyid"));

            internalTransferBean.setFromEmployeeID(rs.getString("internal.fromemployeeid"));
            internalTransferBean.setFromAccountID(rs.getLong("internal.fromaccountid"));
            internalTransferBean.setToEmployeeID(rs.getString("internal.toemployeeid"));
            internalTransferBean.setToAccountID(rs.getLong("internal.toaccountid"));
            internalTransferBean.setIncludeInCalc(rs.getBoolean("internal.includeincalc"));
            internalTransferBean.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));

            return internalTransferBean;
        }
    }

    public void isDuplicateSuspect(InternalTransferBean internalTransferBean) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", internalTransferBean.getOrgId());
        params.put("fromemployeeid", internalTransferBean.getFromEmployeeID());
        params.put("toemployeeid", internalTransferBean.getToEmployeeID());


        params.put("amount", internalTransferBean.getAmount().longValue());

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( select  floor(amount) " +
                " as amount ,id from internaltransfer where orgid=:orgid and fromemployeeid=:fromemployeeid " +
                " and (isdeleted is null or isdeleted<> true)  and toemployeeid = :toemployeeid order by id desc limit 1) " +
                " internaltransfer where amount = :amount", params, Integer.class);


        if (count > 0) {
            throw new CustomAppException("The Internal Transfer looks like duplicate", "SERVER.CREATE_INTERNAL_TRANSFER.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }

    /*
     * API invoked by UI before UpdateSales
     */
    public InternalTransferBean getInternalTransferId(Long orgId, Long purchaseId) {
        java.util.List<SearchParam> searchParams = new ArrayList<SearchParam>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("internal.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue("" + purchaseId);
        searchParams.add(param);
        List<InternalTransferBean> internalTransferBeans = listInternalTransfer(orgId, searchParams, "", 1, 1);
        if (internalTransferBeans != null && !internalTransferBeans.isEmpty()) {
            return internalTransferBeans.get(0);
        }
        throw new CustomAppException("Internal Transfer Looks to be empty", "SERVER.INTERNAL_TRANSFER.NOT_EXISTING", HttpStatus.CONFLICT);
    }

    public int updateInternalTransfer(InternalTransferBean internalTransferBean){
        return namedParameterJdbcTemplate.update("update internaltransfer set details = :details where orgid = :orgId " +
                "and internaltransfer.id = :internalTransferID", new BeanPropertySqlParameterSource(internalTransferBean));
    }
}
