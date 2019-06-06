package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.entity.Card;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardInvalidDataException;
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

        Page<Card> cardPage = cardRepository.findAll(pageable);
        List<CardDto> cardDtos = cardPage.getContent().stream()
            .map(card -> buildCardDto(card))
            .collect(Collectors.toList());

        return new PageImpl<>(cardDtos, pageable, cardPage.getTotalElements());
    }

    public CardDto findById(Long id) throws CardNotFoundException {
        Optional<Card> cardOptional = cardRepository.findById(id);

        if (!cardOptional.isPresent()) throw new CardNotFoundException("Card Not Found !!");

        return buildCardDto(cardOptional.get());
    }

    public CardDto findByPublicId(UUID publicId) throws CardNotFoundException {

        Optional<Card> cardOptional = cardRepository.findByPublicId(publicId);
        if (!cardOptional.isPresent()) throw new CardNotFoundException("Card Not Found !!");

        return buildCardDto(cardOptional.get());
    }

    public CardDto create(CardDto cardDto) throws BuyerNotFoundException {

        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(cardDto.getBuyerDto().getPublicId());

        if(!buyerOptional.isPresent())  throw new BuyerNotFoundException("Buyer Not Found !!");

        Card card = buildCard(cardDto);
        card.setBuyer(buyerOptional.get());
        Card cardSaved = cardRepository.save(card);

        return buildCardDto(cardSaved);
    }

    public CardDto update(UUID uuid,
                          CardDto cardDto) throws CardNotFoundException,
        BuyerNotFoundException, CardInvalidDataException {

        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(cardDto.getBuyerDto().getPublicId());
        if(!buyerOptional.isPresent())  throw new BuyerNotFoundException("Buyer Not Found !!");

        Optional<Card> cardOptional = cardRepository.findByPublicId(uuid);
        if (!cardOptional.isPresent()) throw new CardNotFoundException("Card Not Found !!");

        Card card = cardOptional.get();
        card.setName(cardDto.getName());
        card.setNumber(cardDto.getNumber());
        card.setExpirationDate(cardDto.getExpirationDate());
        card.setCVV(cardDto.getCVV());
        card.setBuyer(buyerOptional.get());
        Card cardSaved = cardRepository.save(card);

        return buildCardDto(cardSaved);
    }

    public void delete(UUID uuid) throws CardNotFoundException {

        Optional<Card> cardOptional = cardRepository.findByPublicId(uuid);
        if (!cardOptional.isPresent()) throw new CardNotFoundException("Card Not Found !!");

        Card card = cardOptional.get();
        cardRepository.delete(card);
    }

    private CardDto buildCardDto(Card card){

        return CardDto.builder()
            .publicId(card.getPublicId())
            .name(card.getName())
            .number(card.getNumber())
            .expirationDate(card.getExpirationDate())
            .CVV(card.getCVV())
            .buyerDto(BuyerDto.builder()
                .publicId(card.getBuyer().getPublicId())
                .name(card.getBuyer().getName())
                .email(card.getBuyer().getEmail())
                .cpf(card.getBuyer().getCpf())
                .build())
            .createdAt(card.getCreatedAt())
            .updatedAt(card.getUpdatedAt())
            .build();
    }

    private Card buildCard(CardDto cardDto) {
        return Card.builder()
            .id(cardDto.getId())
            .publicId(cardDto.getPublicId())
            .name(cardDto.getName())
            .number(cardDto.getNumber())
            .expirationDate(cardDto.getExpirationDate())
            .CVV(cardDto.getCVV())
            .buyer(Buyer.builder()
                .publicId(cardDto.getBuyerDto().getPublicId())
                .build())
            .build();
    }
}
