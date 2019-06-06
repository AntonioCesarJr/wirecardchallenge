package com.wirecardchallenge.rest.controller.payment.validator;

import com.wirecardchallenge.rest.controller.payment.request.PostPaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class PostPaymentRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PostPaymentRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PostPaymentRequest postPaymentRequest = (PostPaymentRequest) o;
        log.info("Validating Payment !!");
    }

}
