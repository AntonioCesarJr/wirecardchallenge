package com.wirecardchallenge.rest.controller.buyer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wirecardchallenge.rest.controller.client.request.ClientRequest;
import lombok.Data;

@Data
public class BuyerRequest {

    private String name;

    private String email;

    private String cpf;

    @JsonProperty(value = "client")
    private ClientRequest clientRequest;
}
