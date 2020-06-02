package com.ANP.util;


import org.springframework.http.HttpStatus;


public class HisabException extends RuntimeException {


    private Exception exception;
    HisabError hisabError;


    public HisabException(HisabError hisabError) {

        this.hisabError = hisabError;
    }

    public HisabException(HisabError hisabError, Exception ex) {

        this.hisabError = hisabError;
        exception = ex;
    }


    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public HisabError getHisabError() {
        return hisabError;
    }

    public void setHisabError(HisabError hisabError) {
        this.hisabError = hisabError;
    }
}
