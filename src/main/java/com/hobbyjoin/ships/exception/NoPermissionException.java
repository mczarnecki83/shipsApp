package com.hobbyjoin.ships.exception;
public class NoPermissionException extends RuntimeException{
    public NoPermissionException(){
        super("error.you_cannot_edit_a_port_that_is_not_yours");
    }
}