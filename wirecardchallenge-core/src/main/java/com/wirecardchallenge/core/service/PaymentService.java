package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public List<PaymentDto> findAll(){

        List<PaymentDto> paymentDtos = paymentRepository.findAll()
                .stream().map(payment -> PaymentDto.builder()
                        .publicId(payment.getPublicId())
                        .build()
                ).collect(Collectors.toList());
        return paymentDtos;
    }

}
