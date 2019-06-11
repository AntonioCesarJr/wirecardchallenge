package com.wirecardchallenge.rest.controller.buyer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wirecardchallenge.rest.controller.client.request.ClientRequest;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BuyerRequest {

    private String name;

    private String email;

    private String cpf;

    private UUID publicId;

    @JsonProperty(value = "clientEntity")
    private ClientRequest clientRequest;
}
