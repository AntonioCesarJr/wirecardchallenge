package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.repository.BuyerRepository;
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
        Buyer buyer = buildBuyer(buyerDto);
        Buyer buyerSaved = buyerRepository.save(buyer);
        return buildBuyerDto(buyerSaved);
    }

    public BuyerDto update(UUID uuid,
                           BuyerDto buyerDto){
        Optional<Buyer> optionalBuyer = buyerRepository.findByPublicId(uuid);
        if (!optionalBuyer.isPresent())
            return BuyerDto.builder().build();
        Buyer buyer = optionalBuyer.get();
        buyer.setName(buyerDto.getName());
        buyer.setEmail(buyerDto.getEmail());
        buyer.setCpf(buyerDto.getCpf());
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
