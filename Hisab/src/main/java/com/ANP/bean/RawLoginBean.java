package com.ANP.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RawLoginBean {
    //UI: registerd Mobile Number currently
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String loginUserName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String orgId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String OTP;

}
