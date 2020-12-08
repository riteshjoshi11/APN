package com.ANP.service;

import com.ANP.bean.AccountBean;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.CustomerDAO;
import com.ANP.bean.CustomerBean;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ANP.util.ANPConstants.LOGIN_TYPE_CUSTOMER;

@Service
public class CustomerHandler {
    @Autowired
    CustomerDAO customerDao;
    @Autowired
    AccountDAO accountDAO;

    private static final Logger logger = LoggerFactory.getLogger(CustomerHandler.class);


    @Transactional(rollbackFor = Exception.class)
    public void createCustomer(CustomerBean customerBean) {
        customerDao.createCustomer(customerBean);

        //Preparing Account & creating account
        AccountBean accountBean = new AccountBean();
        accountBean.setOrgId(customerBean.getOrgId());
        accountBean.setOwnerid(customerBean.getCustomerID());
        accountBean.setAccountnickname(getAccountNickName(customerBean));
        accountBean.setCreatedbyid(customerBean.getCreatedbyId());
        accountBean.setCurrentbalance(customerBean.getInitialBalance());
        accountBean.setInitialBalance(customerBean.getInitialBalance());
        accountBean.setType(ANPConstants.LOGIN_TYPE_CUSTOMER);
        accountDAO.createAccount(accountBean);
    }

    public String getAccountNickName(CustomerBean customerBean) {
        String accountNickNameWithoutMobile;
        String accountNickNameWithoutCity;
        String accountNickName;
        String firmName;
        String name;
        String city;
        String mobile;

        if(ANPUtils.isNullOrEmpty(customerBean.getName())) {
            name = "";
        } else {
            if(customerBean.getName().equals(customerBean.getFirmname())) {
                name = customerBean.getName();
            } else if(customerBean.getFirmname().toUpperCase().contains(customerBean.getName().toUpperCase())){
                name = "";
            } else{
                name = customerBean.getName();
            }
        }

        if(ANPUtils.isNullOrEmpty(customerBean.getFirmname())){
            firmName = "";
        }
        else {
            if(customerBean.getName().equals(customerBean.getFirmname())) {
                firmName = "";
            }else if(customerBean.getName().toUpperCase().contains(customerBean.getFirmname().toUpperCase())){
                firmName = "";
            }
            else {
                firmName = " " + customerBean.getFirmname();
            }
        }

        accountNickNameWithoutCity = name + firmName;

        if(ANPUtils.isNullOrEmpty(customerBean.getCity())){
            city = "";
        } else {
            if(customerBean.getCity().equals(accountNickNameWithoutCity)) {
                city = "";
            } else if(accountNickNameWithoutCity.toUpperCase().contains(customerBean.getCity().toUpperCase())) {
                city = "";
            }
            else {
                city = " " + customerBean.getCity();
            }
        }

        accountNickNameWithoutMobile = name + firmName + city;

        if(accountNickNameWithoutMobile.length() > 185){
            accountNickNameWithoutMobile = accountNickNameWithoutMobile.substring(0,185);
        }


        if(ANPUtils.isNullOrEmpty(customerBean.getMobile1())){
            mobile = "";
        } else {
            mobile = " [" + customerBean.getMobile1() +"]";
        }

        accountNickName = accountNickNameWithoutMobile + mobile;
        logger.trace(accountNickName);
        return accountNickName;
    }


    public void updateCustomer(CustomerBean customerBean){
        CustomerBean customerBeanFetched = customerDao.getCustomerById(customerBean.getOrgId(),customerBean.getCustomerID());
        if(!customerBeanFetched.getName().equalsIgnoreCase(customerBean.getName()) ||
        !customerBeanFetched.getFirmname().equalsIgnoreCase(customerBean.getFirmname()) ||
        !customerBeanFetched.getCity().equalsIgnoreCase(customerBean.getCity())){
            String accountNickName = getAccountNickName(customerBean);
            accountDAO.updateAccountNickName(customerBean.getCustomerID(),customerBean.getOrgId(),accountNickName);
        }
        if( !(customerBean.getInitialBalance()==null && customerBeanFetched.getInitialBalance()==null)
                && (customerBean.getInitialBalance().longValue()!=customerBeanFetched.getInitialBalance().longValue())){
            //This is to update initial balance in the backend.
            accountDAO.updateInitialBalanceField(customerBean.getCustomerID(),customerBean.getOrgId(),customerBean.getInitialBalance());

            AccountBean accountBean = new AccountBean();
            accountBean.setOrgId(customerBean.getOrgId());
            accountBean.setOwnerid(customerBean.getCustomerID());
            logger.trace("AccountId = " + customerBeanFetched.getAccountId());
            accountBean.setAccountId(customerBeanFetched.getAccountId());
            accountBean.setInitialBalance(customerBean.getInitialBalance());
            accountBean.setType(LOGIN_TYPE_CUSTOMER);
            accountDAO.updateInitialBalance(accountBean);
        }
        customerDao.updateCustomer(customerBean);
    }

}
