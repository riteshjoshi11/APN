package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.CustomerDAO;
import com.ANP.repository.OrgDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import com.ANP.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHandler {

    @Autowired
    OrgDAO orgDAO;
    @Autowired
    CustomerDAO customerDao;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    OTPHandler otpHandler;
    @Autowired
    RoleTypeBeanSingleton roleTypeBeanSingleton;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private TokenUtil tokenUtil;
    /*
    1. User Enter Mobile
    2. Send OTP
    3. Validate Mobile Number (Check in 1. Employee table 2. Customer Table) Two approaches #1. Check Employee first then Customer Table #2 Create a view and simply validate
    4. Check if Mobile Number is already registered (#1. As an employee (under an org) #2As an customer (no org)).
    5. if registered: Check if exists under more than one Org. (Develop a method that gives organization(s) for a mobile number)
   */

    //Send OTP on the given mobileNumber
    public boolean sendOTP(String mobileNumber) {
        return otpHandler.sendOTP(mobileNumber);
    }


    public Token validateOTPAndProvideToken(OTPBean otpBean) {
        otpHandler.verifyOTP(otpBean);
        Token token = new Token();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(otpBean.getMobileNumber());
        String tok = tokenUtil.generateToken(userDetails);
        token.setToken(tok);
        return token;
    }


    /*
        This method assumes that the mobileNumber is already OTP verified.
        #1 It check in Employee table if mobile number registered or not. If Yes then all the organization list will be set to the return bean
        #2 If user is not registered as Employee then we will check if it exists as a customer/vendor already
        3# if both are not then the user has not registered in any way
     */
    public IntermediateLoginBean isMobileRegistered(String mobileNumber) {
        IntermediateLoginBean intermediateLoginBean = new IntermediateLoginBean();
        List<Organization> organizationList = orgDAO.getOrganizationsForMobileNo(mobileNumber);
        boolean loginVerified = false;
        if (organizationList != null && organizationList.size() > 0) {
            //The user is registered as an Employee
            intermediateLoginBean.setLoginType(ANPConstants.LOGIN_TYPE_EMPLOYEE);
            intermediateLoginBean.setOrganizationList(organizationList);
            loginVerified = true;

        }
        /*else {
            CustomerBean customerBean = customerDao.getCustomerUsingMobile1(mobileNumber);
            if (customerBean != null && !(ANPUtils.isNullOrEmpty(customerBean.getCustomerID()))) {
                loginVerified = true;
                intermediateLoginBean.setLoginType(ANPConstants.LOGIN_TYPE_CUSTOMER);
            }
        }*/
        intermediateLoginBean.setLoginVerified(loginVerified);
        return intermediateLoginBean;
    }

    public SuccessLoginBean getLoggedInUserDetails(String mobileNumber,long orgId) {

        SuccessLoginBean loginBean = accountDAO.getUserDetails(mobileNumber,orgId);
        if(!loginBean.getEmployeeBean().getLoginrequired()) {
            throw new CustomAppException("The user with given mobile number on the given business is disabled.",
                    "SERVER.getLoggedInUserDetails.LOGIN.DISABLED", HttpStatus.LOCKED);
        }
        if(loginBean.getEmployeeBean().getTypeInt()==6) {
            throw new CustomAppException("The user with given type cannot login.",
                    "SERVER.getLoggedInUserDetails.LOGIN.NOT_ALLOWED", HttpStatus.LOCKED);
        }
        //set the user permissions
        PermissionBean permissionBean = roleTypeBeanSingleton.getPermissionBean(loginBean.getEmployeeBean().getTypeInt());
        loginBean.setPermissionBean(permissionBean);
        return loginBean;
    }

}
