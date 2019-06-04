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
public class CardDto {

    @JsonIgnore
    private Long id;

    private UUID publicId;

    private String name;

    private String number;

    private String expirationDate;

    private String CVV;

    @JsonProperty(value = "buyer")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BuyerDto buyerDto;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime updatedAt;
}
