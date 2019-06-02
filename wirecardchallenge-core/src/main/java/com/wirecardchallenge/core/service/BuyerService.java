package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.entity.Client;
import com.wirecardchallenge.core.repository.BuyerRepository;
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
public class BuyerService {

    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    ClientRepository clientRepository;

    public Page<BuyerDto> findAll(Pageable pageable){
        Page<Buyer> buyerPage = buyerRepository.findAll(pageable);
        List<BuyerDto> buyerDtos= buyerPage.getContent().stream()
            .map(buyer -> buildBuyerDto(buyer))
            .collect(Collectors.toList());
        return new PageImpl<>(buyerDtos, pageable, buyerPage.getTotalElements());
    }

    public BuyerDto findById(Long id){
        Optional<Buyer> buyerOptional = buyerRepository.findById(id);
        if (!buyerOptional.isPresent())
            return BuyerDto.builder().build();
        return buildBuyerDto(buyerOptional.get());
    }

    public BuyerDto findByPublicId(UUID publicId){
        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(publicId);
        if (!buyerOptional.isPresent())
            return BuyerDto.builder().build();
        return buildBuyerDto(buyerOptional.get());
    }

    public BuyerDto create(BuyerDto buyerDto){
        Optional<Client> client = clientRepository.findByPublicId(buyerDto.getClientDto().getPublicId());
        Buyer buyer = buildBuyer(buyerDto);
        buyer.setClient(client.get());
        Buyer buyerSaved = buyerRepository.save(buyer);
        return buildBuyerDto(buyerSaved);
    }

    public BuyerDto update(UUID uuid,
                           BuyerDto buyerDto){
        Optional<Client> client = clientRepository.findByPublicId(buyerDto.getClientDto().getPublicId());
        Optional<Buyer> optionalBuyer = buyerRepository.findByPublicId(uuid);
        if (!optionalBuyer.isPresent())
            return BuyerDto.builder().build();
        Buyer buyer = optionalBuyer.get();
        buyer.setName(buyerDto.getName());
        buyer.setEmail(buyerDto.getEmail());
        buyer.setCpf(buyerDto.getCpf());
        buyer.setClient(client.get());
        Buyer buyerSaved = buyerRepository.save(buyer);
        return buildBuyerDto(buyerSaved);
    }

    public void delete(UUID uuid){
        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(uuid);
        if (buyerOptional.isPresent())
            buyerRepository.delete(buyerOptional.get());
    }

    private BuyerDto buildBuyerDto(Buyer buyer){
        return BuyerDto.builder()
            .publicId(buyer.getPublicId())
            .email(buyer.getEmail())
            .name(buyer.getName())
            .cpf(buyer.getCpf())
            .clientDto(ClientDto.builder().publicId(buyer.getClient().getPublicId()).build())
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
