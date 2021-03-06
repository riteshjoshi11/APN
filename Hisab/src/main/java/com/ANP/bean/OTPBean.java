package com.ANP.bean;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class OTPBean {
    @Pattern(regexp = "\\s*|.{6}", message = "OTP should be 6 characters long")
    private String otp;

    private java.util.Date timeGenerated;

    @Pattern(regexp = "\\s*|.{10}", message = "mobile no. should be of 10 digits")

    private String mobileNumber;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getTimeGenerated() {
        return timeGenerated;
    }

    public void setTimeGenerated(Date timeGenerated) {
        this.timeGenerated = timeGenerated;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
