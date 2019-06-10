package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.entity.BuyerEntity;
import com.wirecardchallenge.core.entity.CardEntity;
import com.wirecardchallenge.core.entity.ClientEntity;
import com.wirecardchallenge.core.entity.PaymentEntity;
import com.wirecardchallenge.core.enumerable.PaymentStatusEnum;
import com.wirecardchallenge.core.enumerable.TypeEnum;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.repository.BuyerRepository;
import com.wirecardchallenge.core.repository.CardRepository;
import com.wirecardchallenge.core.repository.PaymentRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    BuyerRepository buyerRepository;

    @InjectMocks
    PaymentService paymentService;

    @Before
    public void setUp(){MockitoAnnotations.initMocks(this);}

    @Test
    public void findAll() {

        List<PaymentEntity> paymentEntities = PaymentDataTest.buildPaymentEntityList();
        Page<PaymentEntity> paymentEntitiesPage = new PageImpl<>(paymentEntities);

        when(paymentRepository.findAll(any(PageRequest.class)))
            .thenReturn(paymentEntitiesPage);

        Page<PaymentDto> paymentDtos = paymentService.findAll(PageRequest.of(1, 1));

        assertNotNull(paymentDtos);
    }

    @Test
    public void createPaymentCreditCard() throws CardNotFoundException, BuyerNotFoundException {

        CardEntity cardEntity = PaymentDataTest.buildCardEntity();
        BuyerEntity buyerEntity = PaymentDataTest.buildBuyerEntity();

        when(cardRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.of(cardEntity));
        when(buyerRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.of(buyerEntity));
        when(paymentRepository.save(any(PaymentEntity.class)))
            .thenReturn(PaymentDataTest.buildPaymentEntityCreditCard_1());

        PaymentDto paymentCreditCard =
            paymentService.createPaymentCreditCard(PaymentDataTest.buildPaymentDtoCreditCard());

        assertNotNull(paymentCreditCard);
    }

    @Test(expected = BuyerNotFoundException.class)
    public void createPaymentCreditCardBuyerNotFoundException() throws CardNotFoundException, BuyerNotFoundException {

        CardEntity cardEntity = PaymentDataTest.buildCardEntity();

        when(cardRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.of(cardEntity));
        when(buyerRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.empty());

        paymentService.createPaymentCreditCard(PaymentDataTest.buildPaymentDtoCreditCard());
    }

    @Test(expected = CardNotFoundException.class)
    public void createPaymentCreditCardNotFoundException() throws CardNotFoundException, BuyerNotFoundException {

        when(cardRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.empty());

        paymentService.createPaymentCreditCard(PaymentDataTest.buildPaymentDtoCreditCard());
    }

    @Test
    public void createPaymentBankSlip() throws BuyerNotFoundException {

        BuyerEntity buyerEntity = PaymentDataTest.buildBuyerEntity();

        when(buyerRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.of(buyerEntity));
        when(paymentRepository.save(any(PaymentEntity.class)))
            .thenReturn(PaymentDataTest.buildPaymentEntityBankSlip_1());

        PaymentDto paymentCreditCard =
            paymentService.createPaymentBankSlip(PaymentDataTest.buildPaymentDtoBankSlip());

        assertNotNull(paymentCreditCard);
    }

    @Test(expected = BuyerNotFoundException.class)
    public void createPaymentBankSlipBuyerNotFoundException() throws BuyerNotFoundException {

        when(buyerRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.empty());

        paymentService.createPaymentBankSlip(PaymentDataTest.buildPaymentDtoBankSlip());
    }
}

final class PaymentDataTest {

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

    private static final Long PAYMENT_ID_3 = 3L;
    private static final UUID PAYMENT_PUBLIC_ID_3 = UUID.randomUUID();
    private static final BigDecimal PAYMENT_AMOUNT_3 = BigDecimal.valueOf(874.65);
    private static final TypeEnum PAYMENT_TYPE_3 = TypeEnum.CREDIT_CARD;
    private static final PaymentStatusEnum PAYMENT_STATUS_3 = PaymentStatusEnum.COMPLETED;
    private static final LocalDateTime PAYMENT_CREATED_AT_3 = LocalDateTime.now();
    private static final LocalDateTime PAYMENT_UPDATED_AT_3 = LocalDateTime.now().plusMinutes(10);

    private static final Long BUYER_ID_1 = 1L;
    private static final UUID BUYER_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String BUYER_NAME_1 = "José da Silva";
    private static final String BUYER_EMAIL_1 = "jose.silva@wirecard.com";
    private static final String BUYER_CPF_1 = "331.406.850-68";
    private static final LocalDateTime BUYER_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long CLIENT_ID_1 = 4L;
    private static final UUID CLIENT_PUBLIC_ID_1 = UUID.randomUUID();
    private static final LocalDateTime CLIENT_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CLIENT_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long CARD_ID_1 = 1L;
    private static final UUID CARD_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String CARD_NAME_1 = "RAIMUNDO PEREIRA";
    private static final String CARD_NUMBER_1 = "1111222233334444";
    private static final String CARD_EXPIRATION_DATE_1 = "02/21";
    private static final String CARD_CVV_1 = "123";
    private static final LocalDateTime CARD_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CARD_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    static final String DI_EXCEPTION_MESSAGE = "Something wrong is not right !!";
    static final Throwable DI_EXCEPTION_CAUSE = new Throwable();

    static List<PaymentEntity> buildPaymentEntityList(){
        List<PaymentEntity> paymentEntityList = new ArrayList<>();
        paymentEntityList.add(buildPaymentEntityBankSlip_1());
        paymentEntityList.add(buildPaymentEntityBankSlip_2());
        paymentEntityList.add(buildPaymentEntityCreditCard_1());
        return paymentEntityList;
    }

    static PaymentEntity buildPaymentEntityBankSlip_1(){
        return PaymentEntity.builder()
            .id(PAYMENT_ID_1)
            .publicId(PAYMENT_PUBLIC_ID_1)
            .amount(PAYMENT_AMOUNT_1)
            .bankSlipNumber(PAYMENT_BANK_SLIP_NUMBER_1)
            .type(PAYMENT_TYPE_1)
            .buyer(buildBuyerEntity())
            .paymentStatus(PAYMENT_STATUS_1)
            .createdAt(PAYMENT_CREATED_AT_1)
            .updatedAt(PAYMENT_UPDATED_AT_1)
            .build();
    }

    static PaymentEntity buildPaymentEntityBankSlip_2(){
        return PaymentEntity.builder()
            .id(PAYMENT_ID_2)
            .publicId(PAYMENT_PUBLIC_ID_2)
            .amount(PAYMENT_AMOUNT_2)
            .bankSlipNumber(PAYMENT_BANK_SLIP_NUMBER_2)
            .type(PAYMENT_TYPE_2)
            .buyer(buildBuyerEntity())
            .paymentStatus(PAYMENT_STATUS_2)
            .createdAt(PAYMENT_CREATED_AT_2)
            .updatedAt(PAYMENT_UPDATED_AT_2)
            .build();
    }

    static PaymentEntity buildPaymentEntityCreditCard_1(){
        return PaymentEntity.builder()
            .id(PAYMENT_ID_3)
            .publicId(PAYMENT_PUBLIC_ID_3)
            .amount(PAYMENT_AMOUNT_3)
            .type(PAYMENT_TYPE_3)
            .buyer(buildBuyerEntity())
            .card(buildCardEntity())
            .paymentStatus(PAYMENT_STATUS_3)
            .createdAt(PAYMENT_CREATED_AT_3)
            .updatedAt(PAYMENT_UPDATED_AT_3)
            .build();
    }

    static BuyerEntity buildBuyerEntity(){
        return BuyerEntity.builder()
            .id(BUYER_ID_1)
            .publicId(BUYER_PUBLIC_ID_1)
            .name(BUYER_NAME_1)
            .email(BUYER_EMAIL_1)
            .cpf(BUYER_CPF_1)
            .client(buildClientEntity())
            .createdAt(BUYER_CREATED_AT_1)
            .updatedAt(BUYER_UPDATED_AT_1)
            .build();
    }

    static ClientEntity buildClientEntity(){
        return ClientEntity.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLIC_ID_1)
            .createdAt(CLIENT_CREATED_AT_1)
            .updatedAt(CLIENT_UPDATED_AT_1)
            .build();
    }

    static CardEntity buildCardEntity(){
        return CardEntity.builder()
            .id(CARD_ID_1)
            .publicId(CARD_PUBLIC_ID_1)
            .name(CARD_NAME_1)
            .number(CARD_NUMBER_1)
            .expirationDate(CARD_EXPIRATION_DATE_1)
            .CVV(CARD_CVV_1)
            .buyer(buildBuyerEntity())
            .createdAt(CARD_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build();
    }

    static PaymentDto buildPaymentDtoBankSlip(){
        return PaymentDto.builder()
            .id(PAYMENT_ID_1)
            .publicId(PAYMENT_PUBLIC_ID_1)
            .amount(BigDecimal.valueOf(745.65))
            .type(PAYMENT_TYPE_1)
            .paymentStatus(PAYMENT_STATUS_1)
            .cardDto(buildCardDto())
            .bankSlipNumber(PAYMENT_BANK_SLIP_NUMBER_1)
            .buyerDto(buildBuyerDto())
            .createdAt(PAYMENT_CREATED_AT_1)
            .updatedAt(PAYMENT_UPDATED_AT_1)
            .build();
    }

    static PaymentDto buildPaymentDtoCreditCard(){
        return PaymentDto.builder()
            .id(PAYMENT_ID_3)
            .publicId(PAYMENT_PUBLIC_ID_3)
            .amount(BigDecimal.valueOf(235.65))
            .type(PAYMENT_TYPE_3)
            .paymentStatus(PAYMENT_STATUS_3)
            .cardDto(buildCardDto())
            .buyerDto(buildBuyerDto())
            .createdAt(PAYMENT_CREATED_AT_3)
            .updatedAt(PAYMENT_UPDATED_AT_3)
            .build();
    }

    static CardDto buildCardDto(){
        return CardDto.builder()
            .id(CARD_ID_1)
            .publicId(CARD_PUBLIC_ID_1)
            .number(CARD_NUMBER_1)
            .expirationDate(CARD_EXPIRATION_DATE_1)
            .CVV(CARD_CVV_1)
            .name(CARD_NAME_1)
            .buyerDto(buildBuyerDto())
            .createdAt(CARD_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build();
    }

    static BuyerDto buildBuyerDto(){
        return BuyerDto.builder()
            .id(BUYER_ID_1)
            .publicId(BUYER_PUBLIC_ID_1)
            .name(BUYER_NAME_1)
            .email(BUYER_EMAIL_1)
            .cpf(BUYER_CPF_1)
            .clientDto(buildClientDto())
            .createdAt(BUYER_CREATED_AT_1)
            .updatedAt(BUYER_UPDATED_AT_1)
            .build();
    }

    static ClientDto buildClientDto(){
        return ClientDto.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLIC_ID_1)
            .createdAt(CLIENT_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build();
    }
}
