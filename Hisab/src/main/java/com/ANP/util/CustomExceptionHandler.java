package com.ANP.util;

import com.ANP.bean.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(HisabException.class)
    ResponseEntity handleCustomException(HttpServletRequest request, HisabException ex) {
        if (ex.getException() != null) {
            //log exception
        } else {
            //log exception
        }
        return new ResponseEntity<Object>(
                new ErrorResponse(ex.getHisabError().getMessage(), ex.getHisabError().getErrorCode()), ex.getHisabError().getHttpStatus());
    }
}
