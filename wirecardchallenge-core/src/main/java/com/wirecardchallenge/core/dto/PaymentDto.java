package com.wirecardchallenge.core.dto;

import com.wirecardchallenge.core.entity.Card;
import com.wirecardchallenge.core.enun.PaymentStatus;
import com.wirecardchallenge.core.enun.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long id;
    private UUID publicId;
    private BigDecimal amount;
    private Type type;
    private PaymentStatus paymentStatus;
    private Card card;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
