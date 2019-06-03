package com.wirecardchallenge.rest.controller.exception.buyer;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
@NoArgsConstructor
public class BuyerInternalErrorHttpException extends RuntimeException{

    public BuyerInternalErrorHttpException(String message){
        super(message);
    }
}
