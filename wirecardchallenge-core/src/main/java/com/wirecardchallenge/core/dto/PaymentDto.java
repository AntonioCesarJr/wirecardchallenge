package com.wirecardchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wirecardchallenge.core.enumerable.PaymentStatusEnum;
import com.wirecardchallenge.core.enumerable.TypeEnum;
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

    private TypeEnum type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String bankSlipNumber;

    private PaymentStatusEnum paymentStatus;

    @JsonProperty(value = "cardEntity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private CardDto cardDto;

    @JsonProperty(value = "buyerEntity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BuyerDto buyerDto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
