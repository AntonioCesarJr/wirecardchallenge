package com.wirecardchallenge.rest.controller.card;

import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.service.CardService;
import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class CardController {

    @Autowired
    CardService cardService;

    @GetMapping
    public ResponseEntity<Page<CardDto>> findAll(Pageable pageRequest){
        Page<CardDto> cardDtos = cardService.findAll(pageRequest);
        return ResponseEntity.ok(cardDtos);
    }

    @GetMapping(value = "/{publicId}")
    public ResponseEntity<CardDto> findByPublicId(@PathVariable UUID publicId){
        CardDto cardDto = cardService.findByPublicId(publicId);
        return ResponseEntity.ok(cardDto);
    }

    @PostMapping
    public ResponseEntity<CardDto> add(@RequestBody @Valid CardRequest cardRequest){
        CardDto cardDto = buildCardDto(cardRequest);
        CardDto cardDtoSaved = cardService.create(cardDto);
        return ResponseEntity.ok(cardDtoSaved);
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<CardDto> update(@PathVariable UUID publicId,
                                           @RequestBody @Valid CardRequest cardRequest){
        CardDto cardDto = buildCardDto(cardRequest);
        cardDto.setPublicId(publicId);
        CardDto cardDtoSaved = cardService.update(publicId, cardDto);
        return ResponseEntity.ok(cardDtoSaved);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<CardDto> delete(@PathVariable UUID publicId){
        cardService.delete(publicId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private CardDto buildCardDto(CardRequest cardRequest){
        return CardDto.builder()
            .name(cardRequest.getName())
            .number(cardRequest.getNumber())
            .expirationDate(cardRequest.getExpirationDate())
            .CVV(cardRequest.getCVV())
            .build();
    }
}
