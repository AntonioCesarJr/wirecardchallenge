package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.entity.Card;
import com.wirecardchallenge.core.entity.Payment;
import com.wirecardchallenge.core.enumerable.Type;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.repository.CardRepository;
import com.wirecardchallenge.core.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CardRepository cardRepository;

    public Page<PaymentDto> findAll(Pageable pageable){

        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        List<PaymentDto> paymentDtos = paymentPage.getContent().stream()
            .map(payment -> {
                    if (payment.getType()== Type.BANK_SLIP){
                        return buildPaymentDtoSlip(payment);
                    }
                    return buildPaymentDtoWithCard(payment);
                }
            ).collect(Collectors.toList());

        return new PageImpl<>(paymentDtos, pageable, paymentPage.getTotalElements());
    }

    public PaymentDto savePaymentCreditCard(PaymentDto paymentDto){


        return null;
    }

    public PaymentDto save(PaymentDto paymentDto) throws CardNotFoundException {

        Optional<Card> cardOptional = cardRepository.findByPublicId(paymentDto.getCardDto().getPublicId());
        if (!cardOptional.isPresent()) throw new CardNotFoundException("Client Not Found !!");

        Payment payment = buildPaymentEntity(paymentDto);
        payment =  paymentRepository.save(payment);

        if (payment.getType().equals(Type.BANK_SLIP))
            return buildPaymentDtoSlip(payment);

        return buildPaymentDtoWithCard(payment);
    }

    public PaymentDto findByPublicId(UUID publicId){

        Optional<Payment> payment = paymentRepository.findByPublicId(publicId);
        if (payment.isPresent())
//            return buildPaymentDto(payment.get());
            return null;
        return PaymentDto.builder().build();
    }

    private Payment buildPaymentEntity(PaymentDto paymentDto){
        return Payment.builder()
            .id(paymentDto.getId())
            .publicId(paymentDto.getPublicId())
            .amount(paymentDto.getAmount())
            .type(paymentDto.getType())
            .paymentStatus(paymentDto.getPaymentStatus())
            .createdAt(paymentDto.getCreatedAt())
            .updatedAt(paymentDto.getUpdatedAt())
            .build();
    }

    private PaymentDto buildPaymentDtoSlip(Payment payment){
        return PaymentDto.builder()
            .publicId(payment.getPublicId())
            .amount(payment.getAmount())
            .type(payment.getType())
            .paymentStatus(payment.getPaymentStatus())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }

    private PaymentDto buildPaymentDtoWithCard(Payment payment){
        return PaymentDto.builder()
            .publicId(payment.getPublicId())
            .amount(payment.getAmount())
            .type(payment.getType())
            .paymentStatus(payment.getPaymentStatus())
            .cardDto(CardDto.builder()
                .publicId(payment.getCard().getPublicId())
                .number(payment.getCard().getNumber())
                .expirationDate(payment.getCard().getExpirationDate())
                .CVV(payment.getCard().getCVV())
                .name(payment.getCard().getName())
                .buyerDto(BuyerDto.builder()
                    .publicId(payment.getCard().getBuyer().getPublicId())
                    .name(payment.getCard().getBuyer().getName())
                    .email(payment.getCard().getBuyer().getEmail())
                    .cpf(payment.getCard().getBuyer().getCpf())
                    .clientDto(ClientDto.builder()
                        .publicId(payment.getCard().getBuyer().getClient().getPublicId())
                        .build())
                    .build())
                .build())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }
}
