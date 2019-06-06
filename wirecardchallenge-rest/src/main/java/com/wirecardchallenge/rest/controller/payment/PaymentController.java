package com.wirecardchallenge.rest.controller.payment;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.enumerable.Type;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.service.PaymentService;
import com.wirecardchallenge.rest.controller.exception.buyer.BuyerNotFoundHttpException;
import com.wirecardchallenge.rest.controller.exception.card.CardNotFoundHttpException;
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

    @PostMapping(value = "//{type}")
    public ResponseEntity<PaymentDto> add(@RequestBody @Valid PostPaymentRequest postPaymentRequest,
                                          @PathVariable Type type){

        PaymentDto paymentDtoToReturn = new PaymentDto();

        if (type.equals(Type.Bank_Slip))
            paymentDtoToReturn = createBankSlipPaymentDto(postPaymentRequest);

        if (type.equals(Type.Credit_Card))
            paymentDtoToReturn = createCreditCardPaymentDto(postPaymentRequest);

        return ResponseEntity.ok(paymentDtoToReturn);
    }

    private PaymentDto createBankSlipPaymentDto(PostPaymentRequest postPaymentRequest){

        PaymentDto paymentDto = buildPaymentDtoBankSlip(postPaymentRequest);
        PaymentDto paymentDtoSaved;
        try {
            paymentDtoSaved =paymentService.createPaymentBankSlip(paymentDto);
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() +
                " -> PUBLICID =  " + postPaymentRequest.getBuyerPulbicId());
        }

        log.info("New Bank Slip Payment - " + paymentDtoSaved.getPublicId());
        return paymentDtoSaved;
    }

    private PaymentDto createCreditCardPaymentDto(PostPaymentRequest postPaymentRequest) {

        PaymentDto paymentDto = buildPaymentDtoCreditCard(postPaymentRequest);
        PaymentDto paymentDtoSaved;

        try {
            paymentDtoSaved = paymentService.createPaymentCreditCard(paymentDto);
        } catch (CardNotFoundException e) {
            throw new CardNotFoundHttpException(e.getMessage() +
                " -> PUBLICID =  " + postPaymentRequest.getBuyerPulbicId());
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() +
                " -> PUBLICID =  " + postPaymentRequest.getBuyerPulbicId());
        }

        log.info("New Credit Card Payment - " + paymentDtoSaved.getPublicId());
        return paymentDtoSaved;
    }

    private PaymentDto buildPaymentDtoBankSlip(PostPaymentRequest postPaymentRequest){

        return PaymentDto.builder()
            .amount(postPaymentRequest.getAmount())
            .buyerDto(BuyerDto.builder()
                .publicId(postPaymentRequest.getBuyerPulbicId())
                .build())
            .build();
    }

    private PaymentDto buildPaymentDtoCreditCard(PostPaymentRequest postPaymentRequest){

        return PaymentDto.builder()
            .amount(postPaymentRequest.getAmount())
            .cardDto(CardDto.builder()
                .publicId(postPaymentRequest.getCardPublicId())
                .build())
            .buyerDto(BuyerDto.builder()
                .publicId(postPaymentRequest.getBuyerPulbicId())
                .build())
            .build();
    }
}
