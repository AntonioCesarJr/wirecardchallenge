package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.entity.BuyerEntity;
import com.wirecardchallenge.core.entity.CardEntity;
import com.wirecardchallenge.core.entity.PaymentEntity;
import com.wirecardchallenge.core.enumerable.PaymentStatus;
import com.wirecardchallenge.core.enumerable.Type;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.repository.BuyerRepository;
import com.wirecardchallenge.core.repository.CardRepository;
import com.wirecardchallenge.core.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BuyerRepository buyerRepository;

    public Page<PaymentDto> findAll(Pageable pageable){

        Page<PaymentEntity> paymentPage = paymentRepository.findAll(pageable);
        List<PaymentDto> paymentDtos = paymentPage.getContent().stream()
            .map(paymentEntity -> {
                    if (paymentEntity.getType()== Type.Bank_Slip)
                        return buildPaymentDtoBankSlip(paymentEntity);
                    return buildPaymentDtoCreditCard(paymentEntity);
                }
            ).collect(Collectors.toList());

        return new PageImpl<>(paymentDtos, pageable, paymentPage.getTotalElements());
    }

    public PaymentDto createPaymentCreditCard(PaymentDto paymentDto)
        throws CardNotFoundException, BuyerNotFoundException {

        Optional<CardEntity> cardOptional = cardRepository.findByPublicId(paymentDto.getCardDto().getPublicId());
        if(!cardOptional.isPresent()) throw new CardNotFoundException(ExceptionMessages.CARD_NOT_FOUND);

        Optional<BuyerEntity> buyerOptional = buyerRepository.findByPublicId(paymentDto.getBuyerDto().getPublicId());
        if(!buyerOptional.isPresent()) throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);

        PaymentEntity paymentEntity = PaymentEntity.builder()
            .amount(paymentDto.getAmount())
            .card(cardOptional.get())
            .buyer(buyerOptional.get())
            .type(Type.Credit_Card)
            .paymentStatus(PaymentStatus.Success)
            .build();

        PaymentEntity paymentEntitySaved = paymentRepository.save(paymentEntity);

        return buildPaymentDtoCreditCard(paymentEntitySaved);
    }

    public PaymentDto createPaymentBankSlip(PaymentDto paymentDto)
        throws BuyerNotFoundException {

        Optional<BuyerEntity> buyerOptional = buyerRepository.findByPublicId(paymentDto.getBuyerDto().getPublicId());
        if(!buyerOptional.isPresent()) throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);

        PaymentEntity paymentEntity = PaymentEntity.builder()
            .amount(paymentDto.getAmount())
            .buyer(buyerOptional.get())
            .type(Type.Bank_Slip)
            .paymentStatus(PaymentStatus.Pending)
            .build();

        PaymentEntity paymentEntitySaved = paymentRepository.save(paymentEntity);

        return buildPaymentDtoBankSlip(paymentEntitySaved);
    }

    private PaymentDto buildPaymentDtoBankSlip(PaymentEntity paymentEntity){
        return PaymentDto.builder()
            .publicId(paymentEntity.getPublicId())
            .amount(paymentEntity.getAmount())
            .type(paymentEntity.getType())
            .paymentStatus(paymentEntity.getPaymentStatus())
            .bankSlipNumber(paymentEntity.getBankSlipNumber())
            .createdAt(paymentEntity.getCreatedAt())
            .updatedAt(paymentEntity.getUpdatedAt())
            .build();
    }

    private PaymentDto buildPaymentDtoCreditCard(PaymentEntity paymentEntity){
        return PaymentDto.builder()
            .publicId(paymentEntity.getPublicId())
            .amount(paymentEntity.getAmount())
            .type(paymentEntity.getType())
            .paymentStatus(paymentEntity.getPaymentStatus())
            .cardDto(CardDto.builder()
                .publicId(paymentEntity.getCard().getPublicId())
                .number(paymentEntity.getCard().getNumber())
                .expirationDate(paymentEntity.getCard().getExpirationDate())
                .CVV(paymentEntity.getCard().getCVV())
                .name(paymentEntity.getCard().getName())
                .buyerDto(BuyerDto.builder()
                    .publicId(paymentEntity.getCard().getBuyer().getPublicId())
                    .name(paymentEntity.getCard().getBuyer().getName())
                    .email(paymentEntity.getCard().getBuyer().getEmail())
                    .cpf(paymentEntity.getCard().getBuyer().getCpf())
                    .clientDto(ClientDto.builder()
                        .publicId(paymentEntity.getCard().getBuyer().getClient().getPublicId())
                        .build())
                    .build())
                .build())
            .createdAt(paymentEntity.getCreatedAt())
            .updatedAt(paymentEntity.getUpdatedAt())
            .build();
    }
}
