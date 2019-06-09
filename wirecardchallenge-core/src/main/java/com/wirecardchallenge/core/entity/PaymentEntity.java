package com.wirecardchallenge.core.entity;

import com.wirecardchallenge.core.enumerable.PaymentStatus;
import com.wirecardchallenge.core.enumerable.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Payment")
@Table(name = "payment")
@Builder
@Data
@AllArgsConstructor
public class PaymentEntity implements Serializable {

    public PaymentEntity(){};

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @Column(unique = true, updatable = false,columnDefinition = "BINARY(16)",length = 16, nullable = false)
    private UUID publicId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(unique = true, updatable = false)
    private String bankSlipNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Builder.Default
    private CardEntity card;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Builder.Default
    private BuyerEntity buyer;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreateAbstractBaseEntity() {
        this.publicId = UUID.randomUUID();
        this.bankSlipNumber = UUID.randomUUID().toString();
    }
}
