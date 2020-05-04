package com.ANP.service;

import com.ANP.bean.AccountBean;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.CustomerDao;
import com.ANP.bean.CustomerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerHandler {
    @Autowired
    CustomerDao customerDao;
    @Autowired
    AccountDAO accountDAO;


    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomer(CustomerBean customerBean) {
        boolean iscustomercreated = false;
        customerDao.createCustomer(customerBean);

        //Preparing Account & creating account
        AccountBean accountBean = new AccountBean();
        accountBean.setOwnerid(customerBean.getCustomerID());
        accountBean.setAccountnickname(getAccountNickName(customerBean.getFirmname(),customerBean.getName(), customerBean.getCity()));
        accountBean.setCreatedbyid(customerBean.getCreatedbyId());
        accountBean.setCurrentbalance(customerBean.getInitialBalance());
        accountBean.setType("C");
        iscustomercreated = accountDAO.createAccount(accountBean);
        return iscustomercreated;
    }

    public String getAccountNickName(String name, String orgName) {
        String accountNickName = name + orgName;
        if (accountNickName.length() > 25) {
            accountNickName = accountNickName.substring(0, 25);
        }

        return accountNickName;

    }

    private String getAccountNickName(String firmName, String personName, String city) {
        String nickName="";
        if(firmName.trim().equalsIgnoreCase(personName.trim())){
            nickName = firmName + " " + city;
        }
        nickName = personName + " " + firmName + " " + city;
        return nickName;
    }
}
