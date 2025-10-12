package com.pos.exception;

public class UserAlreadyExitedException extends RuntimeException{
    public UserAlreadyExitedException(String message){
        super(message);
    }
}
