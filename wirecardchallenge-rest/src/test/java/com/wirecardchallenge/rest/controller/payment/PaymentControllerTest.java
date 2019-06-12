package com.wirecardchallenge.rest.controller.payment;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.enumerable.PaymentStatusEnum;
import com.wirecardchallenge.core.enumerable.TypeEnum;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.service.CardService;
import com.wirecardchallenge.core.service.PaymentService;
import com.wirecardchallenge.rest.controller.payment.request.PostPaymentRequest;
import com.wirecardchallenge.rest.exception.buyer.BuyerNotFoundHttpException;
import com.wirecardchallenge.rest.exception.card.CardNotFoundHttpException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class PaymentControllerTest {

    private static final Long CARD_ID_1 = 1L;
    private static final UUID CARD_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String CARD_NAME_1 = "João Da Silva";
    private static final String CARD_NUMBER_1 = "1111222233334444";
    private static final String CARD_EXPIRATION_DATE_1 = "10/22";
    private static final String CARD_CVV_1 = "111";
    private static final LocalDateTime CARD_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CARD_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long BUYER_ID_1 = 1L;
    private static final UUID BUYER_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String BUYER_NAME_1 = "José da Silva";
    private static final String BUYER_EMAIL_1 = "jose.silva@wirecard.com";
    private static final String BUYER_CPF_1 = "331.406.850-68";
    private static final LocalDateTime BUYER_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long PAYMENT_ID_1 = 1L;
    private static final UUID PAYMENT_PUBLIC_ID_1 = UUID.randomUUID();
    private static final BigDecimal PAYMENT_AMOUNT_1 = BigDecimal.valueOf(1345.87);
    private static final String PAYMENT_BANK_SLIP_NUMBER_1 = UUID.randomUUID().toString();
    private static final TypeEnum PAYMENT_TYPE_1 = TypeEnum.BANK_SLIP;
    private static final PaymentStatusEnum PAYMENT_STATUS_1 = PaymentStatusEnum.COMPLETED;
    private static final LocalDateTime PAYMENT_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime PAYMENT_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long PAYMENT_ID_2 = 2L;
    private static final UUID PAYMENT_PUBLIC_ID_2 = UUID.randomUUID();
    private static final BigDecimal PAYMENT_AMOUNT_2 = BigDecimal.valueOf(563.22);
    private static final String PAYMENT_BANK_SLIP_NUMBER_2 = UUID.randomUUID().toString();
    private static final TypeEnum PAYMENT_TYPE_2 = TypeEnum.BANK_SLIP;
    private static final PaymentStatusEnum PAYMENT_STATUS_2 = PaymentStatusEnum.PENDING;
    private static final LocalDateTime PAYMENT_CREATED_AT_2 = LocalDateTime.now();
    private static final LocalDateTime PAYMENT_UPDATED_AT_2 = LocalDateTime.now().plusMinutes(10);

    private static final String NOT_FOUND = "RESOURCE NOT FOUND";

    @Mock
    PaymentService paymentService;

    @Mock
    CardService cardService;

    @InjectMocks
    PaymentController paymentController;

    @Before
    public void setUp() {MockitoAnnotations.initMocks(this);}

    @Test
    public void findAll() {

        List<PaymentDto> paymentDtoList = new ArrayList<>();
        Page<PaymentDto> paymentDtoPage = new PageImpl<>(paymentDtoList);

        when(paymentService.findAll(any(Pageable.class)))
            .thenReturn(paymentDtoPage);

        ResponseEntity<Page<PaymentDto>> paymentResponse = paymentController.findAll(PageRequest.of(1, 1));
        assertNotNull(paymentResponse);
    }

    @Test
    public void addBankSlip() throws BuyerNotFoundException {

        when(paymentService.createPaymentBankSlip(any(PaymentDto.class)))
            .thenReturn(buildPaymentDtoBankSlip());

        PostPaymentRequest postPaymentRequest = buildPostPaymentRequest();
        ResponseEntity<PaymentDto> paymentDtoResponse = paymentController.add(postPaymentRequest, TypeEnum.BANK_SLIP);
        assertNotNull(paymentDtoResponse);
    }

    @Test(expected = BuyerNotFoundHttpException.class)
    public void addBankSlipBuyerNotFoundException() throws BuyerNotFoundException {

        when(paymentService.createPaymentBankSlip(any(PaymentDto.class)))
            .thenThrow(new BuyerNotFoundException(NOT_FOUND));

        PostPaymentRequest postPaymentRequest = buildPostPaymentRequest();
        paymentController.add(postPaymentRequest, TypeEnum.BANK_SLIP);
    }

    @Test
    public void addCreditCard() throws BuyerNotFoundException, CardNotFoundException {

        when(paymentService.createPaymentCreditCard(any(PaymentDto.class)))
            .thenReturn(buildPaymentDtoCreditCard());

        when(cardService.findByPublicId(any(UUID.class)))
            .thenReturn(buildCardDto());

        PostPaymentRequest postPaymentRequest = buildPostPaymentRequest();
        ResponseEntity<PaymentDto> paymentDtoResponse = paymentController.add(postPaymentRequest, TypeEnum.CREDIT_CARD);
        assertNotNull(paymentDtoResponse);
    }

    @Test(expected = CardNotFoundHttpException.class)
    public void addCreditCardCardNotFoundException() throws BuyerNotFoundException, CardNotFoundException {

        when(paymentService.createPaymentCreditCard(any(PaymentDto.class)))
            .thenThrow(new CardNotFoundException(NOT_FOUND));

        when(cardService.findByPublicId(any(UUID.class)))
            .thenReturn(buildCardDto());

        PostPaymentRequest postPaymentRequest = buildPostPaymentRequest();
        paymentController.add(postPaymentRequest, TypeEnum.CREDIT_CARD);
    }

    @Test(expected = BuyerNotFoundHttpException.class)
    public void addCreditCardBuyerNotFoundException() throws BuyerNotFoundException, CardNotFoundException {

        when(paymentService.createPaymentCreditCard(any(PaymentDto.class)))
            .thenThrow(new BuyerNotFoundException(NOT_FOUND));

        when(cardService.findByPublicId(any(UUID.class)))
            .thenReturn(buildCardDto());

        PostPaymentRequest postPaymentRequest = buildPostPaymentRequest();
        paymentController.add(postPaymentRequest, TypeEnum.CREDIT_CARD);
    }

    private List<PaymentDto> buildPaymentDtoList(){
        List<PaymentDto> paymentDtos = new ArrayList<>();
        return paymentDtos;
    }

    private PaymentDto buildPaymentDtoBankSlip(){
        return PaymentDto.builder()
            .id(PAYMENT_ID_1)
            .publicId(PAYMENT_PUBLIC_ID_1)
            .amount(PAYMENT_AMOUNT_1)
            .type(PAYMENT_TYPE_1)
            .paymentStatus(PAYMENT_STATUS_1)
            .bankSlipNumber(PAYMENT_BANK_SLIP_NUMBER_1)
            .buyerDto(buildBuyerDto())
            .createdAt(PAYMENT_CREATED_AT_1)
            .updatedAt(PAYMENT_UPDATED_AT_1)
            .build();
    }

    private PaymentDto buildPaymentDtoCreditCard(){
        return PaymentDto.builder()
            .id(PAYMENT_ID_2)
            .publicId(PAYMENT_PUBLIC_ID_2)
            .amount(PAYMENT_AMOUNT_2)
            .type(PAYMENT_TYPE_2)
            .paymentStatus(PAYMENT_STATUS_2)
            .bankSlipNumber(PAYMENT_BANK_SLIP_NUMBER_2)
            .buyerDto(buildBuyerDto())
            .cardDto(buildCardDto())
            .createdAt(PAYMENT_CREATED_AT_2)
            .updatedAt(PAYMENT_UPDATED_AT_2)
            .build();
    }

    public CardDto buildCardDto(){
        return CardDto.builder()
            .id(CARD_ID_1)
            .publicId(CARD_PUBLIC_ID_1)
            .name(CARD_NAME_1)
            .number(CARD_NUMBER_1)
            .expirationDate(CARD_EXPIRATION_DATE_1)
            .CVV(CARD_CVV_1)
            .buyerDto(buildBuyerDto())
            .createdAt(CARD_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build();
    }

    private BuyerDto buildBuyerDto() {
        return BuyerDto.builder()
            .id(BUYER_ID_1)
            .publicId(BUYER_PUBLIC_ID_1)
            .name(BUYER_NAME_1)
            .email(BUYER_EMAIL_1)
            .cpf(BUYER_CPF_1)
            .createdAt(BUYER_CREATED_AT_1)
            .updatedAt(BUYER_UPDATED_AT_1)
            .build();
    }

    private PostPaymentRequest buildPostPaymentRequest(){
        return PostPaymentRequest.builder()
            .amount(PAYMENT_AMOUNT_1)
            .cardPublicId(CARD_PUBLIC_ID_1)
            .buyerPulbicId(BUYER_PUBLIC_ID_1)
            .build();
    }
}
