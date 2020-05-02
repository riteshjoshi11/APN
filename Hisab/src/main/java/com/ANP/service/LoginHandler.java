package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.CustomerDao;
import com.ANP.repository.OrgDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHandler {

    @Autowired
    OrgDAO orgDAO;
    @Autowired
    CustomerDao customerDao;

    public SuccessLoginBean fullLoginWithPassword(RawLoginBean loginBean) {
        //TODO need to think logic of Authorization and return Valid token or login information
        SuccessLoginBean loginSuccess = new SuccessLoginBean();
        loginSuccess.setOrgID("1");
        loginSuccess.setLoggedInEmployeeID("E1");
        loginSuccess.setLoggedInUserAccountID("1");
        loginSuccess.setLoggedInUserType("Employee");
        return loginSuccess;
    }

    boolean isValidFullLogin(RawLoginBean loginBean) {
        //Check with clientID, Username and password you can find a user in the database.
        // Please make sure that ClientID, userName are case-insensitive while password is sensitive.
        return true;
    }

    /*
    1. User Enter Mobile
    2. Send OTP
    3. Validate Mobile Number (Check in 1. Employee table 2. Customer Table) Two approaches #1. Check Employee first then Customer Table #2 Create a view and simply validate
    4. Check if Mobile Number is already registered (#1. As an employee (under an org) #2As an customer (no org)).
    5. if registered: Check if exists under more than one Org. (Develop a method that gives organization(s) for a mobile number)
   */

    //Send OTP on the given mobileNumber
    public boolean sendOTP(String mobileNumber) {
        return true;
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

        } else {
            CustomerBean customerBean = customerDao.getCustomerUsingMobile1(mobileNumber);
            if (customerBean != null && !(ANPUtils.isNullOrEmpty(customerBean.getCustomerID()))) {
                loginVerified = true;
                intermediateLoginBean.setLoginType(ANPConstants.LOGIN_TYPE_CUSTOMER);
            }

        }
        intermediateLoginBean.setLoginVerified(loginVerified);
        return intermediateLoginBean;
    }

}
