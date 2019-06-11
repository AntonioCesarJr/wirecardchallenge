package com.wirecardchallenge.rest.controller.client.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClientRequest {

    private UUID publicId;
}
