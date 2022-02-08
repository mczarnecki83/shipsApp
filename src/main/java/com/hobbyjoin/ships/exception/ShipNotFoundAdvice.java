package com.hobbyjoin.ships.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
@ControllerAdvice
public class ShipNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ShipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String shipNotFoundHandler(ShipNotFoundException ex){
        return ex.getMessage();
    }
}
