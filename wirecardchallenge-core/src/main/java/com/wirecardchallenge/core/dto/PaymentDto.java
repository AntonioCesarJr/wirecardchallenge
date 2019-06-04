package com.wirecardchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wirecardchallenge.core.enumerable.PaymentStatus;
import com.wirecardchallenge.core.enumerable.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @JsonIgnore
    private Long id;

    private UUID publicId;

    private BigDecimal amount;

    private Type type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String bankSlipNumber;

    private PaymentStatus paymentStatus;

    @JsonProperty(value = "card")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private CardDto cardDto;

    @JsonProperty(value = "buyer")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BuyerDto buyerDto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
