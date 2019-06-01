package com.wirecardchallenge.rest.controller.buyer.request;

import lombok.Data;

@Data
public class BuyerRequest {

    private String name;
    private String email;
    private String cpf;
}
