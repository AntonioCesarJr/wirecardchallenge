package com.wirecardchallenge.rest.controller.buyer;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.buyer.BuyerServiceIntegrityConstraintException;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.service.BuyerService;
import com.wirecardchallenge.rest.controller.buyer.request.BuyerRequest;
import com.wirecardchallenge.rest.exception.buyer.BuyerInternalErrorHttpException;
import com.wirecardchallenge.rest.exception.buyer.BuyerNotFoundHttpException;
import com.wirecardchallenge.rest.exception.client.ClientNotFoundHttpException;
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

    public static final String PUBLIC_ID_SYMBOL = " -> PUBLIC_ID =  ";

    @Autowired
    BuyerService buyerService;

    @GetMapping
    public ResponseEntity<Page<BuyerDto>> findAll(Pageable pageRequest){
        Page<BuyerDto> buyerDtos = buyerService.findAll(pageRequest);
        return ResponseEntity.ok(buyerDtos);
    }

    @GetMapping(value = "/{publicId}")
    public ResponseEntity<BuyerDto> findByPublicId(@PathVariable UUID publicId){
        try {
            BuyerDto buyerDto = buyerService.findByPublicId(publicId);
            return ResponseEntity.ok(buyerDto);
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() + PUBLIC_ID_SYMBOL + publicId);
        }
    }

    @PostMapping
    public ResponseEntity<BuyerDto> add(@RequestBody @Valid BuyerRequest buyerRequest){
        BuyerDto buyerDto = buildBuyerDto(buyerRequest);
        try {
            BuyerDto buyerDtoSaved = buyerService.create(buyerDto);
            return ResponseEntity.ok(buyerDtoSaved);
        } catch (ClientNotFoundException e) {
            throw new ClientNotFoundHttpException(e.getMessage() + PUBLIC_ID_SYMBOL +
                buyerRequest.getClientRequest().getPublicId());
        } catch (BuyerServiceIntegrityConstraintException e) {
            throw new BuyerInternalErrorHttpException(e.getMessage());
        }
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<BuyerDto> update(@PathVariable UUID publicId,
                                           @RequestBody @Valid BuyerRequest buyerRequest){
        BuyerDto buyerDto = buildBuyerDto(buyerRequest);
        buyerDto.setPublicId(publicId);
        try {
            BuyerDto buyerDtoSaved = buyerService.update(publicId, buyerDto);
            return ResponseEntity.ok(buyerDtoSaved);
        } catch (ClientNotFoundException e) {
            throw new ClientNotFoundHttpException(e.getMessage() + PUBLIC_ID_SYMBOL +
                buyerRequest.getClientRequest().getPublicId());
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() + PUBLIC_ID_SYMBOL + publicId);
        } catch (BuyerServiceIntegrityConstraintException e) {
            throw new BuyerInternalErrorHttpException(e.getMessage());
        }
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<BuyerDto> delete(@PathVariable UUID publicId){
        try {
            buyerService.delete(publicId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() + PUBLIC_ID_SYMBOL + publicId);
        } catch (BuyerServiceIntegrityConstraintException e) {
            throw new BuyerInternalErrorHttpException(e.getMessage());
        }
    }

    private BuyerDto buildBuyerDto(BuyerRequest buyerRequest){
        return BuyerDto.builder()
            .name(buyerRequest.getName())
            .email(buyerRequest.getEmail())
            .cpf(buyerRequest.getCpf())
            .clientDto(ClientDto.builder()
                .publicId(buyerRequest.getClientRequest().getPublicId())
                .build())
            .build();
    }
}
