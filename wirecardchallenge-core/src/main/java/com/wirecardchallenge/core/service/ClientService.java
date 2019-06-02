package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.Client;
import com.wirecardchallenge.core.exceptions.ClientNotFoundException;
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
        Page<Client> clientPage = clientRepository.findAll(pageable);
        List<ClientDto> clientDtos = clientPage.getContent().stream()
            .map(client -> buildClientDto(client))
            .collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageable, clientPage.getTotalElements());
    }

    public ClientDto findByPublicId(UUID publicId) throws ClientNotFoundException {
        Optional<Client> clientOptional = clientRepository.findByPublicId(publicId);
        if (!clientOptional.isPresent())
            throw new ClientNotFoundException();
        return buildClientDto(clientOptional.get());
    }

    public ClientDto create(ClientDto clientDto){
        Client client = buildClient(clientDto);
        Client clientSaved = clientRepository.save(client);
        return buildClientDto(clientSaved);
    }

    public ClientDto update(UUID publicId,
                            ClientDto clientDto) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientRepository.findByPublicId(publicId);
        if (!optionalClient.isPresent())
            throw new ClientNotFoundException();
        Client client = optionalClient.get();
        Client clientSaved = clientRepository.save(client);
        return buildClientDto(clientSaved);
    }

    public void delete(UUID publicId) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findByPublicId(publicId);
        if (!client.isPresent())
            throw new ClientNotFoundException();
        clientRepository.delete(client.get());
    }

    private ClientDto buildClientDto(Client client){
        return ClientDto.builder()
            .publicId(client.getPublicId())
            .createdAt(client.getCreatedAt())
            .updatedAt(client.getUpdatedAt())
            .build();
    }

    private Client buildClient(ClientDto clientDto){
        return Client.builder()
            .id(clientDto.getId())
            .publicId(clientDto.getPublicId())
            .build();
    }
}
