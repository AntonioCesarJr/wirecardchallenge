package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.entity.Payment;
import com.wirecardchallenge.core.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public List<PaymentDto> findAll(){
        List<PaymentDto> paymentDtos = paymentRepository.findAll()
                .stream().map(payment -> buildPaymentDto(payment)
                ).collect(Collectors.toList());
        return paymentDtos;
    }

    public PaymentDto save(PaymentDto paymentDto){
        Payment payment = buildPaymentEntity(paymentDto);
        payment =  paymentRepository.save(payment);
        return buildPaymentDto(payment);
    }

    public PaymentDto findByPublicId(UUID publicId){
        Optional<Payment> payment = paymentRepository.findByPublicId(publicId);
        if (payment.isPresent())
            return buildPaymentDto(payment.get());
        return PaymentDto.builder().build();
    }

    private Payment buildPaymentEntity(PaymentDto paymentDto){
        Payment paymentToReturn = buildPaymentEntity(paymentDto);
        if (paymentDto.getId() == null){
            paymentToReturn.setId(paymentDto.getId());
            return paymentToReturn;
        }
        return paymentToReturn;
    }

    private PaymentDto buildPaymentDto(Payment payment){
        return PaymentDto.builder()
                .publicId(payment.getPublicId())
                .amount(payment.getAmount())
                .type(payment.getType())
                .paymentStatus(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
