package com.laur.licenta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "Category Not Found")
public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String exMessage){
        super(exMessage);
    }
}
