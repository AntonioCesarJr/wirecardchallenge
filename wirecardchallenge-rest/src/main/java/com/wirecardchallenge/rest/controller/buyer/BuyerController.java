package com.wirecardchallenge.rest.controller.buyer;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.service.BuyerService;
import com.wirecardchallenge.rest.controller.buyer.request.BuyerRequest;
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
@RequestMapping(value = "/api/v1/buyer")
public class BuyerController {

    @Autowired
    BuyerService buyerService;

    @GetMapping
    public ResponseEntity<Page<BuyerDto>> findAll(Pageable pageRequest){
        Page<BuyerDto> buyerDtos = buyerService.findAll(pageRequest);
        return ResponseEntity.ok(buyerDtos);
    }

    @GetMapping(value = "/{publicId}")
    public ResponseEntity<BuyerDto> findByPublicId(@PathVariable UUID publicId){
        BuyerDto buyerDto = buyerService.findByPublicId(publicId);
        return ResponseEntity.ok(buyerDto);
    }

    @PostMapping
    public ResponseEntity<BuyerDto> add(@RequestBody @Valid BuyerRequest buyerRequest){
        BuyerDto buyerDto = buildBuyerDto(buyerRequest);
        BuyerDto buyerDtoSaved = buyerService.create(buyerDto);
        return ResponseEntity.ok(buyerDtoSaved);
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<BuyerDto> update(@PathVariable UUID publicId,
                                           @RequestBody @Valid BuyerRequest buyerRequest){
        BuyerDto buyerDto = buildBuyerDto(buyerRequest);
        buyerDto.setPublicId(publicId);
        BuyerDto buyerDtoSaved = buyerService.update(publicId, buyerDto);
        return ResponseEntity.ok(buyerDtoSaved);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<BuyerDto> delete(@PathVariable UUID publicId){
        buyerService.delete(publicId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private BuyerDto buildBuyerDto(BuyerRequest buyerRequest){
        return BuyerDto.builder()
                .name(buyerRequest.getName())
                .email(buyerRequest.getEmail())
                .cpf(buyerRequest.getCpf())
                .build();
    }
}