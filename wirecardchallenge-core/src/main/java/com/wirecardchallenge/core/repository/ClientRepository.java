package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findAll(Pageable pageable);
    Optional<Client> findById(Long uuid);
    Optional<Client> findByPublicId(String uuid);

}
