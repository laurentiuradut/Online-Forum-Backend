package com.laur.licenta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "Id Not Found")
public class IdNotFoundException extends Exception{
    public IdNotFoundException(String exMessage, Exception exception){
        super(exMessage,exception);
    }
    public IdNotFoundException(String exMessage){
        super(exMessage);
    }
}
