package com.ANP.com.ANP.service;

import com.ANP.bean.LoginBean;
import org.springframework.stereotype.Service;

@Service
public class LoginHandler {

    LoginBean loginUser(LoginBean loginBean){
        //TODO need to think logic of Authorization and return Valid token or login information
        LoginBean loginSuccess=new LoginBean();

        loginSuccess.setOrgID("1");
        loginSuccess.setCompanyId("1");

        return loginSuccess;

    }

    boolean isValidUSer(LoginBean loginBean){
        return true;
    }

}
