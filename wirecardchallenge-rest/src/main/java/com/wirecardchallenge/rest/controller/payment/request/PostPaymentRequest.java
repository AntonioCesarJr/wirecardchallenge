package com.wirecardchallenge.rest.controller.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wirecardchallenge.rest.controller.buyer.request.BuyerRequest;
import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PostPaymentRequest {

    private BigDecimal amount;

    @JsonProperty(value = "card")
    private CardRequest cardRequest;

    @JsonProperty(value = "buyer")
    private PostPaymentBuyerRequest buyerRequest;

}
