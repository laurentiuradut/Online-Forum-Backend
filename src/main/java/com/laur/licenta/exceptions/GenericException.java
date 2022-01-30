package com.laur.licenta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "Exception")
public class GenericException extends RuntimeException {
    public GenericException(String exMessage){super(exMessage);
    }

    public GenericException(String exMessage, Exception e){super(exMessage);
    }


}
