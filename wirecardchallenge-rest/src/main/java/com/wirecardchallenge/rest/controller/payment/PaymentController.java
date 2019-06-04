package com.wirecardchallenge.rest.controller.payment;


import com.wirecardchallenge.core.dto.PaymentDto;
import com.wirecardchallenge.core.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Page<PaymentDto>> listPayments(Pageable pageable){
        Page<PaymentDto> paymentDtos = paymentService.findAll(pageable);
        return ResponseEntity.ok(paymentDtos);
    }




}
