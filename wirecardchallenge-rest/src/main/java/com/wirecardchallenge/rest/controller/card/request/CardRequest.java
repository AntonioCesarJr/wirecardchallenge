package com.wirecardchallenge.rest.controller.card.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    private String name;

    private String number;

    private String expirationDate;

    private String CVV;

    @JsonProperty(value = "buyer.publicId")
    private UUID buyerPublicId;
}
