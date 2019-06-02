package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Payment;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Cacheable("paymentsPage")
    List<Payment> findAll();

    @Cacheable("paymentId")
    Optional<Payment> findById(Long id);

    @Cacheable("paymentPublicId")
    Optional<Payment> findByPublicId(UUID publicId);

    @Caching(evict = {
        @CacheEvict(value="paymentsPage", allEntries=true),
        @CacheEvict(value="paymentId", allEntries=true),
        @CacheEvict(value="paymentPublicId", allEntries=true)})
    Payment save(Payment payment);

    @Caching(evict = {
        @CacheEvict(value="paymentsPage", allEntries=true),
        @CacheEvict(value="paymentId", allEntries=true),
        @CacheEvict(value="paymentPublicId", allEntries=true)})
    void delete(Payment payment);
}
