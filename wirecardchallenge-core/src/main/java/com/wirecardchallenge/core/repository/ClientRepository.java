package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.ClientEntity;
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
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Cacheable("clientsPage")
    Page<ClientEntity> findAll(Pageable pageable);

    @Cacheable("clientId")
    Optional<ClientEntity> findById(Long id);

    @Cacheable("clientPublicId")
    Optional<ClientEntity> findByPublicId(UUID publicId);

    @Caching(evict = {
        @CacheEvict(value="clientsPage", allEntries=true),
        @CacheEvict(value="clientId", allEntries=true),
        @CacheEvict(value="clientPublicId", allEntries=true)})
    ClientEntity save(ClientEntity clientEntity);

    @Caching(evict = {
        @CacheEvict(value="clientsPage", allEntries=true),
        @CacheEvict(value="clientId", allEntries=true),
        @CacheEvict(value="clientPublicId", allEntries=true)})
    void delete(ClientEntity clientEntity);
}
