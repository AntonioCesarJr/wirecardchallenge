package com.wirecardchallenge.rest.controller.exception;

public class CardNotFoundHttpException extends RuntimeException {

    public CardNotFoundHttpException(String message) {
        super(message);
    }
}
