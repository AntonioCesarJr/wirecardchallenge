package com.wirecardchallenge.rest.controller.exception;

public class BuyerNotFoundHttpException extends RuntimeException {

    public BuyerNotFoundHttpException(String message) {
        super(message);
    }
}
