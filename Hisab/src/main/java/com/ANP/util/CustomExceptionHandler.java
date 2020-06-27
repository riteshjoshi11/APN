package com.ANP.util;

import com.ANP.bean.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException; //this is @valid exceptions


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity handleCustomException(HttpServletRequest request, Exception ex) {
        System.out.println("Exception Caught");
        ex.printStackTrace();

        if (ex!= null && ex instanceof CustomAppException ) {
            CustomAppException hisabException = (CustomAppException) ex;
            return new ResponseEntity<Object>(hisabException.getErrorResponse(),hisabException.getHttpStatus());
        } else if(ex instanceof MethodArgumentNotValidException) {
            System.out.println("One of the validation failed");
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) ex;
            System.out.println("App Message=" + ex.getMessage());
            System.out.println("App Cause=" + ex.getCause());
            System.out.println("App Binding=" + ((MethodArgumentNotValidException) ex).getBindingResult());
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            String errorMessageValidation = "";
            if(bindingResult!=null && bindingResult.hasFieldErrors()) {
                System.out.println("There are field errors...");
                List<ObjectError> validationErrors = bindingResult.getAllErrors();
                for(ObjectError error: validationErrors) {
                    errorMessageValidation =  error.getDefaultMessage() ;
                    System.out.println("My Error" + error.getDefaultMessage());
                }
            }

            return new ResponseEntity<Object> (new ErrorResponse(errorMessageValidation,"411"), HttpStatus.BAD_REQUEST);

        }
        System.out.println("Looks like internal server error, we are not going to reveal the issue");
        return new ResponseEntity<Object> (new ErrorResponse("Internal Server Issue","501"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
