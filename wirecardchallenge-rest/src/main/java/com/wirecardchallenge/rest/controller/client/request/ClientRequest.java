package com.wirecardchallenge.rest.controller.client.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClientRequest {

    @NotBlank
    private String name;
    private String description;
}
