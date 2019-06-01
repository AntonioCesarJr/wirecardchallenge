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
    @Cacheable("clientUUID")
    Optional<Client> findByPublicId(UUID uuid);
    @Caching(evict = {
            @CacheEvict(value="clientsPage", allEntries=true),
            @CacheEvict(value="clientsId", key = "#client.id"),
            @CacheEvict(value="clientsUUID", key = "#client.publicId")
    })
    Client save(Client client);
    @Caching(evict = {
            @CacheEvict(value="clientsPage", allEntries=true),
            @CacheEvict(value="clientsId", key = "#client.id"),
            @CacheEvict(value="clientsUUID", key = "#client.publicId")
    })
    void delete(Client client);
}
