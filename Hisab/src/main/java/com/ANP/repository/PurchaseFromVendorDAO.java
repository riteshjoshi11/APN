package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.Expense;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class PurchaseFromVendorDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /*
     *   TODO: Joshi i have corrected this method, you need to run the latest 29April version of DB patch
     *  No need to change anything here.
     */
    public int createBill(PurchaseFromVendorBean purchaseFromVendorBean) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO purchasefromvendor(fromcustomerId,date,CGST,orderamount,SGST,IGST,extra,totalamount,orgId,createdById,note,includeInReport,includeincalc,fromaccountid,billno) " +
                        " VALUES(:fromCustomerId,:date,:CGST,:orderAmount,:SGST,:IGST,:extra,:totalAmount,:orgId,:createdbyId,:note,:includeInReport,:includeInCalc,:fromAccountId,:billNo); ",
                new BeanPropertySqlParameterSource(purchaseFromVendorBean));
    }


    public List<PurchaseFromVendorBean> listPurchasesPaged(long orgID, Collection<SearchParam> searchParams,
                                                           String orderBy, int noOfRecordsToShow, int startIndex) {

        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);


        return namedParameterJdbcTemplate.query("select customer.id,customer.name, customer.city," +
                        "customer.gstin,customer.mobile1,customer.firmname, customer.orgid, " +
                        "p.id, p.date,p.CGST,p.orderamount,p.SGST," +
                        "p.IGST,p.extra,p.totalamount,p.note,p.includeInReport," +
                        "p.includeincalc,p.billno " +
                        " from customer,purchasefromvendor p where p.orgid=:orgId and customer.id=p.id " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
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
            purchaseFromVendorBean.setPurchaseID(rs.getLong("p.id"));
            purchaseFromVendorBean.setDate(rs.getDate("p.date"));
            purchaseFromVendorBean.setCGST(rs.getFloat("p.CGST"));
            purchaseFromVendorBean.setSGST(rs.getFloat("p.SGST"));
            purchaseFromVendorBean.setIGST(rs.getFloat("p.IGST"));
            purchaseFromVendorBean.setExtra(rs.getFloat("p.extra"));
            purchaseFromVendorBean.setTotalAmount(rs.getFloat("p.totalamount"));
            purchaseFromVendorBean.setNote(rs.getString("p.note"));
            purchaseFromVendorBean.setIncludeInReport(rs.getBoolean("p.includeInReport"));
            purchaseFromVendorBean.setIncludeInCalc(rs.getBoolean("p.includeincalc"));
            purchaseFromVendorBean.setBillNo(rs.getString("p.billno"));
            return purchaseFromVendorBean;
        }
    }
}

