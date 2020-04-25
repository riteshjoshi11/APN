package com.ANP.com.ANP.service;

import com.ANP.bean.SuccessLoginBean;
import com.ANP.bean.RawLoginBean;
import org.springframework.stereotype.Service;

@Service
public class LoginHandler {
   public SuccessLoginBean fullLoginWithPassword(RawLoginBean loginBean){
        //TODO need to think logic of Authorization and return Valid token or login information
        SuccessLoginBean loginSuccess=new SuccessLoginBean();
        loginSuccess.setOrgID("1");
        loginSuccess.setCompanyId("C001");
        loginSuccess.setLoggedInEmployeeID("E1");
        loginSuccess.setLoggedInUserAccountID("1");
        loginSuccess.setLoggedInUserType("Employee");
        return loginSuccess;
    }



    boolean isValidFullLogin(RawLoginBean loginBean){
       //Check with clientID, Username and password you can find a user in the database.
       // Please make sure that ClientID, userName are case-insensitive while password is sensitive.
       return true;
    }

}
