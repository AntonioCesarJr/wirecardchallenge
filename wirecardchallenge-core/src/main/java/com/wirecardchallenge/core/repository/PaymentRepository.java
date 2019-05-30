package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAll();
    Optional<Payment> findById(Long id);
    Optional<Payment> findByPublicId(UUID publicId);
}
