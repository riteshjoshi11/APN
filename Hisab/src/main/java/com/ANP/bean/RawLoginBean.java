package com.ANP.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RawLoginBean  {

    //UI: registerd Mobile Number currently
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String mobileNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String OTP;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
