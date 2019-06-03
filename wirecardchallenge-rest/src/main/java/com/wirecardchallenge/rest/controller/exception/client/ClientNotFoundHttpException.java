package com.wirecardchallenge.rest.controller.exception.client;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class ClientNotFoundHttpException extends RuntimeException {

    public ClientNotFoundHttpException(String message) {
        super(message);
    }
}
