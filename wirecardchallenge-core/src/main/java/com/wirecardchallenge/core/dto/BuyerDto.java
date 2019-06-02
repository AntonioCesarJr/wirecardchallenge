package com.wirecardchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDto {

    @JsonIgnore
    private Long id;

    private UUID publicId;

    private String name;

    private String email;

    private String cpf;

    @JsonProperty(value = "client")
    private ClientDto clientDto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
