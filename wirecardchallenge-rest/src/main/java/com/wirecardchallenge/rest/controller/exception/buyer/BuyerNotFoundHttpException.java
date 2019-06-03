package com.wirecardchallenge.rest.controller.exception.buyer;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class BuyerNotFoundHttpException extends RuntimeException {

    public BuyerNotFoundHttpException(String message) {
        super(message);
    }
}
