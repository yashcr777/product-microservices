package com.ecommerceProduct.Product.exceptionHandler;
import com.ecommerceProduct.Product.exceptions.AlreadyExistException;
import com.ecommerceProduct.Product.response.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity handleAlreadyExistException(AlreadyExistException ex) {

        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        response.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}

