package com.ANP.repository;

import com.ANP.bean.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    public List<Customer> getCustomer(){

        List <Customer> customers =new ArrayList<Customer>();
        Customer cu1=new Customer();
        cu1.setName("Ritesh");
        cu1.setCity("test dummy");
        cu1.setOrgid(1);

        Customer cu2=new Customer();
        cu2.setName("Nitesh");
        cu2.setCity("test dummy2");
        cu2.setOrgid(2);
        customers.add(cu1);
        customers.add(cu2);
        return customers;

    }



    public int createCustomer(Customer customer) {
        System.out.println("customer "+customer.getName());
        return namedParameterJdbcTemplate.update(
                "insert into customer (name, city,orgId) values(:name,:city,:orgId)",
                new BeanPropertySqlParameterSource(customer));

    }


    public List<Customer> findByNameAndCity(Customer customer) {
        String where = "";
        if (null != customer.getName() && (null != customer.getCity())) {
            where = "name = :name and city =:city";
        } else if (null != customer.getName()) {
            where = "name = :name";
        } else if (null != customer.getCity()) {
            where = "city =:city";
        }


        return namedParameterJdbcTemplate.query(
                "select * from customer where " + where,
                new BeanPropertySqlParameterSource(customer), new CustomerMapper());



    }

    private static final class CustomerMapper implements RowMapper<Customer> {
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer cus = new Customer();
            cus.setId(rs.getLong("id"));
            cus.setName(rs.getString("name"));
            cus.setCity(rs.getString("city"));
            return cus;
        }
    }

    }
