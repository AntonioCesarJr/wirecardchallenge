package com.wirecardchallenge.rest.controller.exception.card;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
@NoArgsConstructor
public class CardInvalidDataHttpException extends RuntimeException{
    public CardInvalidDataHttpException(String message){
        super(message);
    }
}
