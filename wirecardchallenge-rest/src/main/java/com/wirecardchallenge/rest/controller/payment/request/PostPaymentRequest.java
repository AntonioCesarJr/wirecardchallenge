package com.wirecardchallenge.rest.controller.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PostPaymentRequest {

    private BigDecimal amount;

    @JsonProperty(value = "cardEntity.publicId")
    private UUID cardPublicId;

    @JsonProperty(value = "buyerEntity.publicId")
    private UUID buyerPulbicId;

}
