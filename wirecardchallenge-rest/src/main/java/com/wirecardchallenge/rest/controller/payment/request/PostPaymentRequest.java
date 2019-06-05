package com.wirecardchallenge.rest.controller.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PostPaymentRequest {

    private BigDecimal amount;

    @JsonProperty(value = "card.publicId")
    private UUID cardPublicId;

    @JsonProperty(value = "buyer.publicId")
    private UUID buyerPulbicId;

}
