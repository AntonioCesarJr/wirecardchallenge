package com.wirecardchallenge.rest.controller.exception.payment;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
@NoArgsConstructor
public class PaymentHttpException extends RuntimeException{

    public PaymentHttpException(String message){
        super(message);
    }
}
