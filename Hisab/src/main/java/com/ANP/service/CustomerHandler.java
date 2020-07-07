package com.ANP.service;

import com.ANP.bean.AccountBean;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.CustomerDAO;
import com.ANP.bean.CustomerBean;
import com.ANP.util.ANPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerHandler {
    @Autowired
    CustomerDAO customerDao;
    @Autowired
    AccountDAO accountDAO;


    @Transactional(rollbackFor = Exception.class)
    public void createCustomer(CustomerBean customerBean) {
        customerDao.createCustomer(customerBean);

        //Preparing Account & creating account
        AccountBean accountBean = new AccountBean();
        accountBean.setOrgId(customerBean.getOrgId());
        accountBean.setOwnerid(customerBean.getCustomerID());
        accountBean.setAccountnickname(getAccountNickName(customerBean.getFirmname(),customerBean.getName(), customerBean.getCity()));
        accountBean.setCreatedbyid(customerBean.getCreatedbyId());
        accountBean.setCurrentbalance(customerBean.getInitialBalance());
        accountBean.setType(ANPConstants.LOGIN_TYPE_CUSTOMER);
        accountDAO.createAccount(accountBean);
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
