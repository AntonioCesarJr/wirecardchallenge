package com.wirecardchallenge.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Card")
@Table(name = "card")
@Builder
@Data
@AllArgsConstructor
public class Card implements Serializable {

    public Card(){}

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @Column(unique = true, updatable = false,columnDefinition = "BINARY(16)",length = 16, nullable = false)
    private UUID publicId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 16)
    private String number;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @Column(nullable = false, length = 3)
    private String CVV;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "card")
    @JsonIgnore
    private Set<Payment> payments;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Builder.Default
    private Buyer buyer;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreateAbstractBaseEntity() {
        this.publicId = UUID.randomUUID();
    }
}
