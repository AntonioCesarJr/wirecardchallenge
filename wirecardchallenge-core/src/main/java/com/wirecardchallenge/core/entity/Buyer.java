package com.wirecardchallenge.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "buyer")
@Data
@NoArgsConstructor
public class  Buyer implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    @Column(name = "public_id", nullable = false)
    private UUID publicId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String cpf;
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @Column(name = "updated_at", columnDefinition = "DATETIME")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreateAbstractBaseEntity() {
        this.publicId = UUID.randomUUID();
    }
}
