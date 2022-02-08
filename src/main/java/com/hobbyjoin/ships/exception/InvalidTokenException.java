package com.hobbyjoin.ships.exception;
public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(){
        super("error.invalid_token");
    }
}