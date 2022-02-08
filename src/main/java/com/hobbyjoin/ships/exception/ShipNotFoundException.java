package com.hobbyjoin.ships.exception;
public class ShipNotFoundException extends RuntimeException{
    public ShipNotFoundException(int id){
        super("Could not find ship: "+id);
    }
}