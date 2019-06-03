package com.wirecardchallenge.rest.controller.card.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CardRequest {

    private String name;

    private String number;

    private String expirationDate;

    private String CVV;

    @JsonProperty(value = "buyer.publicId")
    private UUID buyerPublicId;
}
