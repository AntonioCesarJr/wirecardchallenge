package com.wirecardchallenge.rest.controller.payment;


import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.enumerable.Type;
import com.wirecardchallenge.core.service.PaymentService;
import com.wirecardchallenge.rest.controller.payment.request.PostPaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Page<PaymentDto>> listPayments(Pageable pageable){
        Page<PaymentDto> paymentDtos = paymentService.findAll(pageable);
        return ResponseEntity.ok(paymentDtos);
    }

    @PostMapping(value = "/{type}")
    public ResponseEntity<PaymentDto> add(@RequestBody @Valid PostPaymentRequest postPaymentRequest,
                                          @PathVariable Type type){
        PaymentDto paymentDto;
        PaymentDto paymentDtoSaved = new PaymentDto();

        if (type.equals(Type.BANK_SLIP)){

        }

        if (type.equals(Type.CREDIT_CARD)) {
            paymentDto = buildPaymentDtoCard(postPaymentRequest);
            paymentDtoSaved = paymentService.savePaymentCreditCard(paymentDto);
            log.info("Save Payment " + paymentDtoSaved.getPublicId());
        }

        return ResponseEntity.ok(paymentDtoSaved);
    }

    private PaymentDto buildPaymentDtoCard(PostPaymentRequest postPaymentRequest){
        return PaymentDto.builder()
            .amount(postPaymentRequest.getAmount())
            .cardDto(CardDto.builder()
                .name(postPaymentRequest.getCardRequest().getName())
                .number(postPaymentRequest.getCardRequest().getNumber())
                .CVV(postPaymentRequest.getCardRequest().getCVV())
                .expirationDate(postPaymentRequest.getCardRequest().getExpirationDate())
                .buyerDto(BuyerDto.builder()
                    .publicId(postPaymentRequest.getCardRequest().getBuyerPublicId())
                    .build())
                .build())
            .build();
    }
}
