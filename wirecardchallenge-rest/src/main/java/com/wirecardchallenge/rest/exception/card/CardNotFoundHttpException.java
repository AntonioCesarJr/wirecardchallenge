package com.wirecardchallenge.rest.exception.card;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class CardNotFoundHttpException extends RuntimeException {

    public CardNotFoundHttpException(String message) {
        super(message);
    }
}
