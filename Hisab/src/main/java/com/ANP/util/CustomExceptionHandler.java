package com.ANP.util;

import com.ANP.bean.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity handleCustomException(HttpServletRequest request, Exception ex) {
        System.out.println("Exception Caught");

        if (ex!= null && ex instanceof HisabException ) {
            HisabException hisabException = (HisabException) ex;
            return new ResponseEntity<Object>(
                    new ErrorResponse(hisabException.getHisabError().getMessage(), hisabException.getHisabError().getErrorCode()),
                    hisabException.getHisabError().getHttpStatus());
        }
        System.out.println("Nitesh: Looks like internal server error, we are not going to reveal the issue");
        return new ResponseEntity<Object> (new ErrorResponse("Internal Server Issue","101"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
