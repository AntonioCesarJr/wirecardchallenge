package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.PaymentEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Cacheable("paymentsPage")
    Page<PaymentEntity> findAll(Pageable pageable);

    @Cacheable("paymentId")
    Optional<PaymentEntity> findById(Long id);

    @Cacheable("paymentPublicId")
    Optional<PaymentEntity> findByPublicId(UUID publicId);

    @Caching(evict = {
        @CacheEvict(value="paymentsPage", allEntries=true),
        @CacheEvict(value="paymentId", allEntries=true),
        @CacheEvict(value="paymentPublicId", allEntries=true)})
    PaymentEntity save(PaymentEntity paymentEntity);

    @Caching(evict = {
        @CacheEvict(value="paymentsPage", allEntries=true),
        @CacheEvict(value="paymentId", allEntries=true),
        @CacheEvict(value="paymentPublicId", allEntries=true)})
    void delete(PaymentEntity paymentEntity);
}
