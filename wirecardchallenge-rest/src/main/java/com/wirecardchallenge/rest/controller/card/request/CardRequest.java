package com.wirecardchallenge.rest.controller.card.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CardRequest {

    private String name;

    private String number;

    private String expirationDate;

    private String CVV;

    @JsonProperty(value = "buyerEntity.publicId")
    private UUID buyerPublicId;
}
