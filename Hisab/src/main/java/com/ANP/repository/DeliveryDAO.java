package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.DeliveryBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DeliveryDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public boolean createDelivery(DeliveryBean deliveryBean) {
        boolean result = false;
        int accountCreated =
         namedParameterJdbcTemplate.update(
                "insert into delivery (tocustomerid,description,date,orgid,createdbyid,createdate)" +
                        " values(:toCustomerID,:description,:date,:orgId,:createdbyId,:createDate)",
                        new BeanPropertySqlParameterSource(deliveryBean));
        if (accountCreated > 0) {
            result = true;
        }
        return result;
    }

    public List<DeliveryBean> listDeliveriesPaged(long orgID, Collection<SearchParam> searchParams,
                                                  String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow",noOfRecordsToShow);
        param.put("startIndex",startIndex-1);
        param.put("orderBy",orderBy);

        return namedParameterJdbcTemplate.query("select customer.name, customer.city," +
                        "customer.gstin,customer.mobile1,customer.firmname, " +
                        " delivery.id, delivery.date,delivery.description " +
                        " from customer,delivery where customer.id=delivery.tocustomerid and delivery.orgid=:orgID " +
                         ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                         + " offset :startIndex",
                     param, new DeliveryDAO.FullDeliveryMapper()) ;
    }

    private static final class FullDeliveryMapper implements RowMapper<DeliveryBean> {
        public DeliveryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            DeliveryBean deliveryBean = new DeliveryBean();
            deliveryBean.getCustomerBean().setName(rs.getString("customer.name"));
            deliveryBean.getCustomerBean().setCity(rs.getString("customer.city"));
            deliveryBean.getCustomerBean().setGstin(rs.getString("customer.gstin"));
            deliveryBean.getCustomerBean().setMobile1(rs.getString("customer.mobile1"));
            deliveryBean.getCustomerBean().setFirmname(rs.getString("customer.firmname"));
            deliveryBean.setDeliveryID(rs.getLong("delivery.id"));
            deliveryBean.setDate(rs.getTimestamp("delivery.date"));
            deliveryBean.setDescription(rs.getString("delivery.description"));
            return deliveryBean;
        }
    }

}
