package com.wirecardchallenge.rest.exception.client;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
@NoArgsConstructor
public class ClientInternalErrorHttpException extends RuntimeException{

    public ClientInternalErrorHttpException(String message){
        super(message);
    }
}
