package com.ANP.repository;

import com.ANP.bean.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDao {

    public List<Customer> getCustomer(){

        List <Customer> customers =new ArrayList<Customer>();
        Customer cu1=new Customer();
        cu1.setName("Ritesh");
        cu1.setCity(1);
        cu1.setOrgId(1);

        Customer cu2=new Customer();
        cu2.setName("Nitesh");
        cu2.setCity(2);
        cu2.setOrgId(2);
        customers.add(cu1);
        customers.add(cu2);
        return customers;

    }
}
