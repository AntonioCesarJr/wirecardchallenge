package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.ClientEntity;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public Page<ClientDto> findAll(Pageable pageable){
        Page<ClientEntity> clientPage = clientRepository.findAll(pageable);
        List<ClientDto> clientDtos = clientPage.getContent().stream()
            .map(this::buildClientDto)
            .collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageable, clientPage.getTotalElements());
    }

    public ClientDto findByPublicId(UUID publicId) throws ClientNotFoundException {
        Optional<ClientEntity> clientOptional = clientRepository.findByPublicId(publicId);
        if (!clientOptional.isPresent()) throw new ClientNotFoundException(ExceptionMessages.CLIENT_NOT_FOUND);
        return buildClientDto(clientOptional.get());
    }

    public ClientDto create(){
        ClientEntity clientEntitySaved = clientRepository.save(ClientEntity.builder().build());
        return buildClientDto(clientEntitySaved);
    }

    public void delete(UUID publicId) throws ClientNotFoundException {
        Optional<ClientEntity> client = clientRepository.findByPublicId(publicId);
        if (!client.isPresent()) throw new ClientNotFoundException(ExceptionMessages.CLIENT_NOT_FOUND);
        clientRepository.delete(client.get());
    }

    private ClientDto buildClientDto(ClientEntity clientEntity){
        return ClientDto.builder()
            .publicId(clientEntity.getPublicId())
            .createdAt(clientEntity.getCreatedAt())
            .updatedAt(clientEntity.getUpdatedAt())
            .build();
    }
}
