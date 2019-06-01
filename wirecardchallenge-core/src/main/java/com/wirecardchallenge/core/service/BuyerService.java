package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.entity.Client;
import com.wirecardchallenge.core.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
