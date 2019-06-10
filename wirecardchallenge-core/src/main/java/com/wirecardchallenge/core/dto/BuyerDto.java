package com.wirecardchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String cpf;

    @JsonProperty(value = "clientEntity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ClientDto clientDto;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime updatedAt;
}
