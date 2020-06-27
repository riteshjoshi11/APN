package com.ANP.util;

import com.ANP.bean.ErrorResponse;
import org.springframework.http.HttpStatus;
/*
 * This is common custom exception class
 *
 */
public class CustomAppException extends RuntimeException {
    //we want to always ensure a common errorCode
    private String errorCode;
    private HttpStatus httpStatus;

    public CustomAppException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode=errorCode;
        this.httpStatus=httpStatus;
    }

    public CustomAppException(String message,Throwable t, String errorCode) {
        super(message,t);
        this.errorCode=errorCode;
    }

    public String getErrorCode() {
        return errorCode;
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

    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(getMessage(),errorCode);
    }
}
