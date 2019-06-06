package com.wirecardchallenge.rest.exception.card;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT)
@NoArgsConstructor
public class CardAndBuyerDoesNotMatchException extends RuntimeException {
    public CardAndBuyerDoesNotMatchException(String message){
        super(message);
    }
}
