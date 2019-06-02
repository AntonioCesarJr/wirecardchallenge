package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.entity.Card;
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

    public Page<CardDto> findAll(Pageable pageable){
        Page<Card> vardPage = cardRepository.findAll(pageable);
        List<CardDto> cardDtos= vardPage.getContent().stream()
            .map(card -> buildCardDto(card))
            .collect(Collectors.toList());
        return new PageImpl<>(cardDtos, pageable, vardPage.getTotalElements());
    }

    public CardDto findById(Long id){
        Optional<Card> cardOptional = cardRepository.findById(id);
        if (!cardOptional.isPresent())
            return CardDto.builder().build();
        return buildCardDto(cardOptional.get());
    }

    public CardDto findByPublicId(UUID publicId){
        Optional<Card> cardOptional = cardRepository.findByPublicId(publicId);
        if (!cardOptional.isPresent())
            return CardDto.builder().build();
        return buildCardDto(cardOptional.get());
    }

    public CardDto create(CardDto cardDto){
        Card card = buildCard(cardDto);
        Card cardSaved = cardRepository.save(card);
        return buildCardDto(cardSaved);
    }

    public CardDto update(UUID uuid,
                          CardDto cardDto){
        Optional<Card> cardOptional = cardRepository.findByPublicId(uuid);
        if (!cardOptional.isPresent())
            return CardDto.builder().build();
        Card card = cardOptional.get();
        card.setName(cardDto.getName());
        card.setNumber(cardDto.getNumber());
        card.setExpirationDate(cardDto.getExpirationDate());
        card.setCVV(cardDto.getCVV());
        Card cardSaved = cardRepository.save(card);
        return buildCardDto(cardSaved);
    }

    public void delete(UUID uuid){
        Optional<Card> cardOptional = cardRepository.findByPublicId(uuid);
        if (cardOptional.isPresent())
            cardRepository.delete(cardOptional.get());
    }

    private CardDto buildCardDto(Card card){
        return CardDto.builder()
            .publicId(card.getPublicId())
            .name(card.getName())
            .number(card.getNumber())
            .expirationDate(card.getExpirationDate())
            .CVV(card.getCVV())
            .createdAt(card.getCreatedAt())
            .updatedAt(card.getUpdatedAt())
            .build();
    }

    private Card buildCard(CardDto cardDto){
        return Card.builder()
            .id(cardDto.getId())
            .publicId(cardDto.getPublicId())
            .name(cardDto.getName())
            .number(cardDto.getNumber())
            .expirationDate(cardDto.getExpirationDate())
            .CVV(cardDto.getCVV())
            .build();
    }
}
