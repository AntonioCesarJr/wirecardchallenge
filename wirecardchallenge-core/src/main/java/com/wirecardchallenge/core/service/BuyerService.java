package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.entity.Client;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.buyer.BuyerServiceIntegrityConstraintException;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.repository.BuyerRepository;
import com.wirecardchallenge.core.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BuyerService {

    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    ClientRepository clientRepository;

    public Page<BuyerDto> findAll(Pageable pageable){

        Page<Buyer> buyerPage = buyerRepository.findAll(pageable);
        List<BuyerDto> buyerDtos = buyerPage.getContent().stream()
            .map(buyer -> buildBuyerDto(buyer))
            .collect(Collectors.toList());

        return new PageImpl<>(buyerDtos, pageable, buyerPage.getTotalElements());
    }

    public BuyerDto findById(Long id) throws BuyerNotFoundException {

        Optional<Buyer> buyerOptional = buyerRepository.findById(id);
        if (!buyerOptional.isPresent()) throw new BuyerNotFoundException("Buyer Not Found !!");

        return buildBuyerDto(buyerOptional.get());
    }

    public BuyerDto findByPublicId(UUID publicId) throws BuyerNotFoundException {

        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(publicId);

        if (!buyerOptional.isPresent()) throw new BuyerNotFoundException("Buyer Not Found !!");
        return buildBuyerDto(buyerOptional.get());
    }

    public BuyerDto create(BuyerDto buyerDto) throws ClientNotFoundException,
        BuyerServiceIntegrityConstraintException {

        Optional<Client> client = clientRepository.findByPublicId(buyerDto.getClientDto().getPublicId());
        if(!client.isPresent()) throw new ClientNotFoundException("Client Not Found !!");

        Buyer buyer = buildBuyer(buyerDto);
        return saveBuyerOrThrow(client.get(), buyer);
    }

    public BuyerDto update(UUID uuid,
                           BuyerDto buyerDto) throws ClientNotFoundException,

        BuyerNotFoundException, BuyerServiceIntegrityConstraintException {

        Optional<Client> client = clientRepository.findByPublicId(buyerDto.getClientDto().getPublicId());

        if(!client.isPresent()) throw new ClientNotFoundException("Client Not Found !!");
        Optional<Buyer> optionalBuyer = buyerRepository.findByPublicId(uuid);

        if (!optionalBuyer.isPresent()) throw new BuyerNotFoundException("Buyer Not Found !!");

        Buyer buyer = optionalBuyer.get();
        buyer.setName(buyerDto.getName());
        buyer.setEmail(buyerDto.getEmail());
        buyer.setCpf(buyerDto.getCpf());
        return saveBuyerOrThrow(client.get(), buyer);
    }

    public void delete(UUID uuid) throws BuyerNotFoundException, BuyerServiceIntegrityConstraintException {

        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(uuid);

        if (!buyerOptional.isPresent()) throw new BuyerNotFoundException("Buyer Not Found !!");

        Buyer buyer = buyerOptional.get();
        try{
            buyerRepository.delete(buyer);
        }catch (DataIntegrityViolationException e){
            log.error(e.getMessage() + " // " + e.getCause().getCause());
            throw new BuyerServiceIntegrityConstraintException(e.getMessage());
        }
    }

    private BuyerDto saveBuyerOrThrow(Client client, Buyer buyer)
        throws BuyerServiceIntegrityConstraintException {
        buyer.setClient(client);
        Buyer buyerSaved;
        try {
            buyerSaved = buyerRepository.save(buyer);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage() + " // " + e.getCause().getCause());
            throw new BuyerServiceIntegrityConstraintException(e.getMessage());
        }

        return buildBuyerDto(buyerSaved);
    }

    private BuyerDto buildBuyerDto(Buyer buyer){
        return BuyerDto.builder()
            .publicId(buyer.getPublicId())
            .email(buyer.getEmail())
            .name(buyer.getName())
            .cpf(buyer.getCpf())
            .clientDto(ClientDto.builder()
                .publicId(buyer.getClient().getPublicId())
                .build())
            .createdAt(buyer.getCreatedAt())
            .updatedAt(buyer.getUpdatedAt())
            .build();
    }

    private Buyer buildBuyer(BuyerDto buyerDto){
        return Buyer.builder()
            .id(buyerDto.getId())
            .publicId(buyerDto.getPublicId())
            .email(buyerDto.getEmail())
            .name(buyerDto.getName())
            .cpf(buyerDto.getCpf())
            .build();
    }
}
