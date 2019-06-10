package com.wirecardchallenge.rest.controller.card;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.service.CardService;
import com.wirecardchallenge.core.service.ExceptionMessages;
import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import com.wirecardchallenge.rest.controller.card.validator.CardRequestValidator;
import com.wirecardchallenge.rest.exception.buyer.BuyerNotFoundHttpException;
import com.wirecardchallenge.rest.exception.card.CardNotFoundHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/card")
@Slf4j
public class CardController {

    @Autowired
    CardService cardService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new CardRequestValidator());
    }

    @GetMapping
    public ResponseEntity<Page<CardDto>> findAll(Pageable pageRequest){
        Page<CardDto> cardDtos = cardService.findAll(pageRequest);
        return ResponseEntity.ok(cardDtos);
    }

    @GetMapping(value = "/{publicId}")
    public ResponseEntity<CardDto> findByPublicId(@PathVariable UUID publicId){
        try {
            CardDto  cardDto = cardService.findByPublicId(publicId);
            return ResponseEntity.ok(cardDto);
        } catch (CardNotFoundException e) {
            throw new CardNotFoundHttpException(ExceptionMessages.CARD_NOT_FOUND + " - " + publicId);
        }
    }

    @PostMapping
    public ResponseEntity<CardDto> add(@Valid @RequestBody CardRequest cardRequest){
        CardDto cardDto = buildCardDto(cardRequest);
        try {
            CardDto cardDtoSaved = cardService.create(cardDto);
            return ResponseEntity.ok(cardDtoSaved);
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(ExceptionMessages.BUYER_NOT_FOUND +
                " - " + cardRequest.getBuyerPublicId());
        }
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<CardDto> update(@Valid @RequestBody CardRequest cardRequest,
                                          @PathVariable UUID publicId){
        CardDto cardDto = buildCardDto(cardRequest);
        cardDto.setPublicId(publicId);
        try {
            CardDto cardDtoSaved = cardService.update(publicId, cardDto);
            return ResponseEntity.ok(cardDtoSaved);
        } catch (CardNotFoundException e) {
            throw new CardNotFoundHttpException(ExceptionMessages.CARD_NOT_FOUND + " - " + publicId);
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(ExceptionMessages.BUYER_NOT_FOUND +
                cardRequest.getBuyerPublicId());
        }
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<CardDto> delete(@PathVariable UUID publicId){
        try {
            cardService.delete(publicId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CardNotFoundException e) {
            throw new CardNotFoundHttpException(ExceptionMessages.CARD_NOT_FOUND + " - " + publicId);
        }
    }

    private CardDto buildCardDto(CardRequest cardRequest){

        return CardDto.builder()
            .name(cardRequest.getName())
            .number(cardRequest.getNumber())
            .expirationDate(cardRequest.getExpirationDate())
            .CVV(cardRequest.getCVV())
            .buyerDto(BuyerDto.builder()
                .publicId(cardRequest.getBuyerPublicId())
                .build())
            .build();
    }
}
