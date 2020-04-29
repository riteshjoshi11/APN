package com.ANP.service;

import com.ANP.bean.AccountBean;
import com.ANP.bean.Organization;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.CustomerDao;
import com.ANP.bean.Customer;
import com.ANP.repository.OrgDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerHandler {
    @Autowired
    CustomerDao customerDao;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    OrgDAO orgDAO;

    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomer(Customer customerBean) {
        boolean iscustomercreated = false;

        customerDao.createCustomer(customerBean);

        AccountBean accountBean = new AccountBean();
        accountBean.setOwnerid(customerBean.getCustomerID());
        Organization org = orgDAO.getOrganization(customerBean.getOrgId());
        accountBean.setAccountnickname(getAccountNickName(customerBean.getName(), org.getOrgname()));
        accountBean.setCreatedbyid(customerBean.getCustomerID());
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
}
