package com.ANP.util;

import org.springframework.http.HttpStatus;


public enum HisabError {
    //HTTP Error code will be  send in response so choose what Http Error code you want to send
    INVALID_TOKEN("Missing or invalid token", HttpStatus.BAD_REQUEST),
    INVALID_OTP("invalid OTP", "H101", HttpStatus.OK);


    private String message;
    private String errorCode;
    private HttpStatus httpStatus;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    HisabError(String message, String errorCode, HttpStatus httpStatus) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    HisabError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.errorCode = httpStatus + "";
    }


    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }


}
