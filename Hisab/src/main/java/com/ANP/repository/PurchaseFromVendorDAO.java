package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;



import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;



import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;





@Repository
public class PurchaseFromVendorDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /*
     *   TODO: Joshi i have corrected this method, you need to run the latest 29April version of DB patch
     *  No need to change anything here.
     */
    public int createBill(PurchaseFromVendorBean purchaseFromVendorBean) {
        if(!purchaseFromVendorBean.isForceCreate()) {
            isDuplicateSuspect(purchaseFromVendorBean);
        }
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


        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "p.id desc";
        }

        return namedParameterJdbcTemplate.query("select customer.id,customer.name, customer.city," +
                        "customer.gstin,customer.mobile1,customer.firmname, customer.orgid, customer.state, " +
                        "p.id, p.date,p.CGST,p.orderamount,p.SGST," +
                        "p.IGST,p.extra,p.totalamount,p.note,p.includeInReport," +
                        "p.includeincalc,p.billno,p.createdate,p.createdbyid " +
                        " from customer,purchasefromvendor p where p.orgid=:orgId and customer.id=p.fromcustomerid and " +
                        " (p.isdeleted is null or p.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by "+ orderBy+" limit  :noOfRecordsToShow"
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
            purchaseFromVendorBean.setPurchaseID(rs.getLong("p.id"));
            purchaseFromVendorBean.setDate(rs.getTimestamp("p.date"));
            purchaseFromVendorBean.setCGST(rs.getFloat("p.CGST"));
            purchaseFromVendorBean.setSGST(rs.getFloat("p.SGST"));
            purchaseFromVendorBean.setIGST(rs.getFloat("p.IGST"));
            purchaseFromVendorBean.setExtra(rs.getFloat("p.extra"));
            purchaseFromVendorBean.setTotalAmount(rs.getFloat("p.totalamount"));
            purchaseFromVendorBean.setNote(rs.getString("p.note"));
            purchaseFromVendorBean.setIncludeInReport(rs.getBoolean("p.includeInReport"));
            purchaseFromVendorBean.setIncludeInCalc(rs.getBoolean("p.includeincalc"));
            purchaseFromVendorBean.setBillNo(rs.getString("p.billno"));
            purchaseFromVendorBean.setCreateDate(rs.getTimestamp("p.createdate"));
            purchaseFromVendorBean.setCreatedbyId(rs.getString("p.createdbyid"));
            return purchaseFromVendorBean;
        }
    }

    public void isDuplicateSuspect(PurchaseFromVendorBean purchaseFromVendorBean){
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String,Object> params = new HashMap<>();
        params.put("orgid", purchaseFromVendorBean.getOrgId());
        params.put("fromcustomerid", purchaseFromVendorBean.getFromCustomerId());

        long actualamount = (long)(purchaseFromVendorBean.getTotalAmount());
        params.put("amount", actualamount);

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( SELECT  floor(totalamount) as totalamount " +
                ",id FROM purchasefromvendor where orgid=:orgid and fromcustomerid=:fromcustomerid and (isdeleted is null or isdeleted <> true) " +
                "  order by id desc limit 1) purchase where totalamount = :amount",params, Integer.class);
        System.out.println(count);
        if(count>0) {
            throw new CustomAppException("The purchase from vendor looks like duplicate", "SERVER.CREATE_PURCHASE_ENTRY.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }
    public List<PurchaseFromVendorBean> pdfListPurchasesPaged(long orgID, Collection<SearchParam> searchParams,
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


        List<PurchaseFromVendorBean> purchaseFromVendorBeanList =  namedParameterJdbcTemplate.query("select * from purchasefromvendor p where " +
                        "includeinreport = true and orgid = :orgId and (isdeleted is null or isdeleted <> true) "+
                ANPUtils.getWhereClause(searchParams) + " order by " + orderBy + " limit  :noOfRecordsToShow"
                        + " offset :startIndex",param, new PDFPurchaseFromVendorMapper());

        DateFormat dateFormat = new SimpleDateFormat();
        com.itextpdf.text.List list ;
        try {

            String file1 = "f:/";
            Document document = new Document( PageSize.A4, 20, 20, 20, 20 );
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            PdfWriter.getInstance(document, new FileOutputStream(file1+ "purchase"+(new Date().getTime()/1000)+".pdf"));
           // PdfWriter.getInstance(document, new FileOutputStream(file1 +  "purchase" + dateFormat.format(new Date()) + ".pdf" ));
            int index = 0;
            document.open();
            for(Object listIterator : purchaseFromVendorBeanList) {
                list = new com.itextpdf.text.List(false );
                list.setListSymbol("");
                list.add(new ListItem("Name :  "));
                list.add(new ListItem("Date:  " + purchaseFromVendorBeanList.get(index).getDate().toString()));
                list.add(new ListItem("Total Amount:  "+purchaseFromVendorBeanList.get(index).getTotalAmount()));
                list.add(new ListItem("Extra:  "+purchaseFromVendorBeanList.get(index).getExtra()));
                list.add(new ListItem("BillNo. :"+purchaseFromVendorBeanList.get(index).getBillNo()));
                list.add(new ListItem("Note:  "+purchaseFromVendorBeanList.get(index).getNote()));
                list.add(new ListItem("IGST:  "+purchaseFromVendorBeanList.get(index).getIGST()));
                list.add(new ListItem("CGST:  "+purchaseFromVendorBeanList.get(index).getCGST()));
                list.add(new ListItem("SGST:  "+purchaseFromVendorBeanList.get(index).getSGST()));
                list.add(new ListItem("From Customer ID:  "+purchaseFromVendorBeanList.get(index).getFromCustomerId()));
                list.add(new ListItem("From Account ID:  "+purchaseFromVendorBeanList.get(index).getFromAccountId()));
                document.add(list);
                document.add(new Paragraph("\n"));
                index++;
            }
            document.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return purchaseFromVendorBeanList;
    }

    private static final class PDFPurchaseFromVendorMapper implements RowMapper<PurchaseFromVendorBean>{
        public PurchaseFromVendorBean mapRow(ResultSet rs,int rowNum) throws SQLException{
            PurchaseFromVendorBean purchaseFromVendorBean = new PurchaseFromVendorBean();
            purchaseFromVendorBean.setPurchaseID(rs.getLong("p.id"));
            purchaseFromVendorBean.setDate(rs.getTimestamp("p.date"));
            purchaseFromVendorBean.setCGST(rs.getFloat("p.CGST"));
            purchaseFromVendorBean.setSGST(rs.getFloat("p.SGST"));
            purchaseFromVendorBean.setIGST(rs.getFloat("p.IGST"));
            purchaseFromVendorBean.setExtra(rs.getFloat("p.extra"));
            purchaseFromVendorBean.setTotalAmount(rs.getFloat("p.totalamount"));
            purchaseFromVendorBean.setOrderAmount(rs.getFloat("p.orderamount"));
            purchaseFromVendorBean.setNote(rs.getString("p.note"));
            purchaseFromVendorBean.setIncludeInReport(rs.getBoolean("p.includeInReport"));
            purchaseFromVendorBean.setIncludeInCalc(rs.getBoolean("p.includeincalc"));
            purchaseFromVendorBean.setBillNo(rs.getString("p.billno"));
            purchaseFromVendorBean.setCreateDate(rs.getTimestamp("p.createdate"));
            purchaseFromVendorBean.setCreatedbyId(rs.getString("p.createdbyid"));
            purchaseFromVendorBean.setFromCustomerId(rs.getString("p.fromcustomerid"));
            purchaseFromVendorBean.setFromAccountId(rs.getLong("p.fromaccountid"));
            return purchaseFromVendorBean;
        }
    }

    public int updatePurchaseGST(PurchaseFromVendorBean purchaseFromVendorBean){
        return namedParameterJdbcTemplate.update("update purchasefromvendor set cgst = :CGST," +
                "sgst=:SGST, igst=:IGST where orgid = :orgId",new BeanPropertySqlParameterSource(purchaseFromVendorBean));
    }


}