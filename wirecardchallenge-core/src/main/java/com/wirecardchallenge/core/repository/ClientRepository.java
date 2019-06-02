package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Client;
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
public interface ClientRepository extends JpaRepository<Client, Long> {


    @Cacheable("clientsPage")
    Page<Client> findAll(Pageable pageable);

    @Cacheable("clientId")
    Optional<Client> findById(Long id);

    @Cacheable("clientPublicId")
    Optional<Client> findByPublicId(UUID publicId);

    @Caching(evict = {
            @CacheEvict(value="clientsPage", allEntries=true),
            @CacheEvict(value="clientId", allEntries=true),
            @CacheEvict(value="clientPublicId", allEntries=true)})
    Client save(Client client);

    @Caching(evict = {
            @CacheEvict(value="clientsPage", allEntries=true),
            @CacheEvict(value="clientId", allEntries=true),
            @CacheEvict(value="clientPublicId", allEntries=true)})
    void delete(Client client);
}
