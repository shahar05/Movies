package com.company;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class OrderNotFoundException extends RuntimeException {
    @ControllerAdvice
    static class OrderNotFoundHandler {
        @ResponseBody //this advice is rendered straight into the response body.
        @ExceptionHandler(OrderNotFoundException.class)
        //configures the advice to only respond if an ProductNotFoundException
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String OrderNotFoundHandler(OrderNotFoundException ex) {
            return ex.getMessage();
        }
    }

    OrderNotFoundException(Long id) {
        super("Order " + id + " was not found");
    }
    OrderNotFoundException(Long id , String customerName) {
        super("This Order with: " + id + " is already have been purchased by " + customerName );
    }
    OrderNotFoundException(Double price) {
        super("Order  with Price:  " + price + " was not found");
    }
}
