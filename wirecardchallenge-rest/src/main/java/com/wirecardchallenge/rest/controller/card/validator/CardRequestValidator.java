package com.wirecardchallenge.rest.controller.card.validator;

import com.wirecardchallenge.core.exceptions.card.CardInvalidDataException;
import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Slf4j
public class CardRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CardRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        log.info("Card Validator in action !!");
        CardRequest cardRequest = (CardRequest) o;
        log.info("Card Name is " + cardRequest.getName());
    }

    private LocalDate buildExpirationDate(String strDate) throws CardInvalidDataException {
        LocalDate localDate = null;
        if (strDate.length() > 5) {
            log.warn("Invalid Length !!");
            throw new CardInvalidDataException("Invalid Expiration Date (MM/yy)!!");
        }
        try{
            String month = strDate.substring(0,2);
            String year = strDate.substring(3,5);
            localDate = LocalDate.of(Integer.valueOf("20"+year),
                Integer.valueOf(month),1);
        }catch (Exception e){
            log.warn("Invalid Date Format (MM/yy) !!");
            throw new CardInvalidDataException("Invalid Expiration Date (MM/yy)!!");
        }
        return localDate;
    }
}
