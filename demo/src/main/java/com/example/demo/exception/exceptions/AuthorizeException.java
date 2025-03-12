package com.example.demo.exception.exceptions;

public class AuthorizeException extends RuntimeException{

    public AuthorizeException(String message){
        super(message);
    }
}
