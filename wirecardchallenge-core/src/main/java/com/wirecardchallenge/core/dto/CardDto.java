package com.wirecardchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
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
    private BuyerDto buyerDto;

    private Set<Payment> payments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
