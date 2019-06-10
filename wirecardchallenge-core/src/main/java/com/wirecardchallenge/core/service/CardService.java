package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.entity.BuyerEntity;
import com.wirecardchallenge.core.entity.CardEntity;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.repository.BuyerRepository;
import com.wirecardchallenge.core.repository.CardRepository;
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
public class CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BuyerRepository buyerRepository;

    public Page<CardDto> findAll(Pageable pageable){

        Page<CardEntity> cardPage = cardRepository.findAll(pageable);
        List<CardDto> cardDtos = cardPage.getContent().stream()
            .map(this::buildCardDto)
            .collect(Collectors.toList());

        return new PageImpl<>(cardDtos, pageable, cardPage.getTotalElements());
    }

    public CardDto findByPublicId(UUID publicId) throws CardNotFoundException {

        Optional<CardEntity> cardOptional = cardRepository.findByPublicId(publicId);
        if (!cardOptional.isPresent()) throw new CardNotFoundException(ExceptionMessages.CARD_NOT_FOUND);

        return buildCardDto(cardOptional.get());
    }

    public CardDto create(CardDto cardDto) throws BuyerNotFoundException {

        Optional<BuyerEntity> buyerOptional = buyerRepository.findByPublicId(cardDto.getBuyerDto().getPublicId());

        if(!buyerOptional.isPresent())  throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);

        CardEntity cardEntity = buildCard(cardDto);
        cardEntity.setBuyer(buyerOptional.get());
        CardEntity cardEntitySaved = cardRepository.save(cardEntity);

        return buildCardDto(cardEntitySaved);
    }

    public CardDto update(UUID uuid,
                          CardDto cardDto) throws CardNotFoundException, BuyerNotFoundException {

        Optional<BuyerEntity> buyerOptional = buyerRepository.findByPublicId(cardDto.getBuyerDto().getPublicId());
        if(!buyerOptional.isPresent())  throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);

        Optional<CardEntity> cardOptional = cardRepository.findByPublicId(uuid);
        if (!cardOptional.isPresent()) throw new CardNotFoundException(ExceptionMessages.CARD_NOT_FOUND);

        CardEntity cardEntity = cardOptional.get();
        cardEntity.setName(cardDto.getName());
        cardEntity.setNumber(cardDto.getNumber());
        cardEntity.setExpirationDate(cardDto.getExpirationDate());
        cardEntity.setCVV(cardDto.getCVV());
        cardEntity.setBuyer(buyerOptional.get());
        CardEntity cardEntitySaved = cardRepository.save(cardEntity);

        return buildCardDto(cardEntitySaved);
    }

    public void delete(UUID uuid) throws CardNotFoundException {

        Optional<CardEntity> cardOptional = cardRepository.findByPublicId(uuid);
        if (!cardOptional.isPresent()) throw new CardNotFoundException(ExceptionMessages.CARD_NOT_FOUND);

        CardEntity cardEntity = cardOptional.get();
        cardRepository.delete(cardEntity);
    }

    private CardDto buildCardDto(CardEntity cardEntity){

        return CardDto.builder()
            .publicId(cardEntity.getPublicId())
            .name(cardEntity.getName())
            .number(cardEntity.getNumber())
            .expirationDate(cardEntity.getExpirationDate())
            .CVV(cardEntity.getCVV())
            .buyerDto(BuyerDto.builder()
                .publicId(cardEntity.getBuyer().getPublicId())
                .name(cardEntity.getBuyer().getName())
                .email(cardEntity.getBuyer().getEmail())
                .cpf(cardEntity.getBuyer().getCpf())
                .build())
            .createdAt(cardEntity.getCreatedAt())
            .updatedAt(cardEntity.getUpdatedAt())
            .build();
    }

    private CardEntity buildCard(CardDto cardDto) {
        return CardEntity.builder()
            .id(cardDto.getId())
            .publicId(cardDto.getPublicId())
            .name(cardDto.getName())
            .number(cardDto.getNumber())
            .expirationDate(cardDto.getExpirationDate())
            .CVV(cardDto.getCVV())
            .buyer(BuyerEntity.builder()
                .publicId(cardDto.getBuyerDto().getPublicId())
                .build())
            .build();
    }
}
