package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Client;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    Optional<Client> findById(Long id);
    Optional<Client> findByPublicId(UUID uuid);
    @CacheEvict(value="clientsPage", allEntries=true)
    Client save(Client client);
    @CacheEvict(value="clientsPage", allEntries=true)
    void delete(Client client);
}
