package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<CustomerBean> getCustomer() {
        List<CustomerBean> customerBeans = new ArrayList<CustomerBean>();
        CustomerBean cu1 = new CustomerBean();
        cu1.setName("Ritesh");
        cu1.setCity("test dummy");
        cu1.setOrgId(1);
        CustomerBean cu2 = new CustomerBean();
        cu2.setName("Nitesh");
        cu2.setCity("test dummy2");
        cu2.setOrgId(2);
        customerBeans.add(cu1);
        customerBeans.add(cu2);
        return customerBeans;
    }

    public int createCustomer(CustomerBean customerBean) {
        System.out.println("customer " + customerBean.getName());
        KeyHolder holder = new GeneratedKeyHolder();
        return namedParameterJdbcTemplate.update(
                "insert into customer (name,city,gstin,transporter,mobile1,mobile2,firmname,billingadress,orgid,createdbyid) " +
                        "values(:name,:city,:gstin,:transporter,:mobile1,:mobile2,:firmname,:billingadress,:orgid,:userID)",
                new BeanPropertySqlParameterSource(customerBean));
    }

    public List<CustomerBean> findByNameAndCity(CustomerBean customerBean) {
        String where = "";
        if (null != customerBean.getName() && (null != customerBean.getCity())) {
            where = "name = :name and city =:city";
    } else if (null != customerBean.getName()) {
            where = "name = :name";
        } else if (null != customerBean.getCity()) {
            where = "city =:city";
        }

        return namedParameterJdbcTemplate.query(
                "select * from customer where " + where,
                new BeanPropertySqlParameterSource(customerBean), new CustomerMapper());
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
}
