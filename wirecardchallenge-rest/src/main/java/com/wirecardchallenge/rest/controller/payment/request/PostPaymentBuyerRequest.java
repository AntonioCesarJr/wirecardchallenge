package com.wirecardchallenge.rest.controller.payment.request;

import lombok.Data;

import java.util.UUID;

@Data
public class PostPaymentBuyerRequest {

    private UUID publicId;
}
