package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.entity.Buyer;
import com.wirecardchallenge.core.entity.Card;
import com.wirecardchallenge.core.entity.Payment;
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

        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        List<PaymentDto> paymentDtos = paymentPage.getContent().stream()
            .map(payment -> {
                    if (payment.getType()== Type.Bank_Slip){
                        return buildPaymentDtoBankSlip(payment);
                    }
                    return buildPaymentDtoCreditCard(payment);
                }
            ).collect(Collectors.toList());

        return new PageImpl<>(paymentDtos, pageable, paymentPage.getTotalElements());
    }

    public PaymentDto createPaymentCreditCard(PaymentDto paymentDto)
        throws CardNotFoundException, BuyerNotFoundException {

        Optional<Card> cardOptional = cardRepository.findByPublicId(paymentDto.getCardDto().getPublicId());
        if(!cardOptional.isPresent()) throw new CardNotFoundException("Card Not Found !!");

        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(paymentDto.getBuyerDto().getPublicId());
        if(!buyerOptional.isPresent()) throw new BuyerNotFoundException("Buyer Not Found !!");

        Payment payment = Payment.builder()
            .amount(paymentDto.getAmount())
            .card(cardOptional.get())
            .buyer(buyerOptional.get())
            .type(Type.Credit_Card)
            .paymentStatus(PaymentStatus.Success)
            .build();

        Payment paymentSaved = paymentRepository.save(payment);

        return buildPaymentDtoCreditCard(paymentSaved);
    }

    public PaymentDto createPaymentBankSlip(PaymentDto paymentDto)
        throws BuyerNotFoundException {

        Optional<Buyer> buyerOptional = buyerRepository.findByPublicId(paymentDto.getBuyerDto().getPublicId());
        if(!buyerOptional.isPresent()) throw new BuyerNotFoundException("Buyer Not Found !!");

        Payment payment = Payment.builder()
            .amount(paymentDto.getAmount())
            .buyer(buyerOptional.get())
            .type(Type.Bank_Slip)
            .paymentStatus(PaymentStatus.Pending)
            .build();

        Payment paymentSaved = paymentRepository.save(payment);

        return buildPaymentDtoBankSlip(paymentSaved);
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

    private PaymentDto buildPaymentDtoBankSlip(Payment payment){
        return PaymentDto.builder()
            .publicId(payment.getPublicId())
            .amount(payment.getAmount())
            .type(payment.getType())
            .paymentStatus(payment.getPaymentStatus())
            .bankSlipNumber(payment.getBankSlipNumber())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }

    private PaymentDto buildPaymentDtoCreditCard(Payment payment){
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
