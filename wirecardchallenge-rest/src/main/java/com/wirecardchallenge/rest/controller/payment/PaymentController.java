package com.wirecardchallenge.rest.controller.payment;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.enumerable.TypeEnum;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.service.CardService;
import com.wirecardchallenge.core.service.PaymentService;
import com.wirecardchallenge.rest.controller.payment.request.PostPaymentRequest;
import com.wirecardchallenge.rest.controller.payment.validator.PostPaymentRequestValidator;
import com.wirecardchallenge.rest.exception.buyer.BuyerNotFoundHttpException;
import com.wirecardchallenge.rest.exception.card.CardAndBuyerDoesNotMatchException;
import com.wirecardchallenge.rest.exception.card.CardNotFoundHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/payment")
@Slf4j
public class PaymentController {

    private static final String PUBLIC_ID_SYMBOL = " -> PUBLICID =  ";
    private static final String CREDIT_CARD_AND_BUYER_NOT_MATCH = "Credit Card and Buyer does not match !!  ";
    private static final String CREDIT_CARD_NOT_FOUND = "Credit Card Not found !!  ";

    @Autowired
    CardService cardService;

    @Autowired
    private PaymentService paymentService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){webDataBinder.addValidators(new PostPaymentRequestValidator());}

    @GetMapping
    public ResponseEntity<Page<PaymentDto>> findAll(Pageable pageable){
        Page<PaymentDto> paymentDtos = paymentService.findAll(pageable);
        return ResponseEntity.ok(paymentDtos);
    }

    @PostMapping(value = "/{typeEnum}")
    public ResponseEntity<PaymentDto> add(@Valid @RequestBody PostPaymentRequest postPaymentRequest,
                                          @PathVariable TypeEnum typeEnum){

        PaymentDto paymentDtoToReturn = new PaymentDto();

        if (typeEnum.equals(TypeEnum.BANK_SLIP))
            paymentDtoToReturn = createBankSlipPaymentDto(postPaymentRequest);

        if (typeEnum.equals(TypeEnum.CREDIT_CARD)) {
            validateCardIssuer(postPaymentRequest.getCardPublicId(), postPaymentRequest.getBuyerPulbicId());
            paymentDtoToReturn = createCreditCardPaymentDto(postPaymentRequest);
        }

        return ResponseEntity.ok(paymentDtoToReturn);
    }

    private PaymentDto createBankSlipPaymentDto(@RequestBody PostPaymentRequest postPaymentRequest){

        PaymentDto paymentDto = buildPaymentDtoBankSlip(postPaymentRequest);
        try {
            PaymentDto paymentDtoSaved = paymentService.createPaymentBankSlip(paymentDto);
            log.info("New Bank Slip PaymentEntity - " + paymentDtoSaved.getPublicId());
            return paymentDtoSaved;
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() +
                PUBLIC_ID_SYMBOL + postPaymentRequest.getBuyerPulbicId());
        }

    }

    private PaymentDto createCreditCardPaymentDto(PostPaymentRequest postPaymentRequest) {

        PaymentDto paymentDto = buildPaymentDtoCreditCard(postPaymentRequest);
        try {
            PaymentDto  paymentDtoSaved = paymentService.createPaymentCreditCard(paymentDto);
            log.info("New Credit CardEntity PaymentEntity - " + paymentDtoSaved.getPublicId());
            return paymentDtoSaved;
        } catch (CardNotFoundException e) {
            throw new CardNotFoundHttpException(e.getMessage() +
                PUBLIC_ID_SYMBOL + postPaymentRequest.getBuyerPulbicId());
        } catch (BuyerNotFoundException e) {
            throw new BuyerNotFoundHttpException(e.getMessage() +
                PUBLIC_ID_SYMBOL + postPaymentRequest.getBuyerPulbicId());
        }
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

    private void validateCardIssuer(UUID cardPublicId, UUID buyerPublicId){
        try {
            CardDto  cardDto = cardService.findByPublicId(cardPublicId);
            if (!cardDto.getBuyerDto().getPublicId().equals(buyerPublicId))
                throw new CardAndBuyerDoesNotMatchException(CREDIT_CARD_AND_BUYER_NOT_MATCH);
        } catch (CardNotFoundException e) {
            throw new CardNotFoundHttpException(CREDIT_CARD_NOT_FOUND + e.getMessage());
        }
    }
}
