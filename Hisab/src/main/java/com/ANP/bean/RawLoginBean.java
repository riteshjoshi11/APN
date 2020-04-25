package com.ANP.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RawLoginBean {
    //UI: registerd Mobile Number currently
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String loginUserName;
    //UI: password
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String loginPassword;
    //UI: clientID
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String clientID;

}
