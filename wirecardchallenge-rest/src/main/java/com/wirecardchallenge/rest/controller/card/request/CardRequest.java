package com.wirecardchallenge.rest.controller.card.request;

import lombok.Data;

@Data
public class CardRequest {

    private String name;

    private String number;

    private String expirationDate;

    private String CVV;
}
