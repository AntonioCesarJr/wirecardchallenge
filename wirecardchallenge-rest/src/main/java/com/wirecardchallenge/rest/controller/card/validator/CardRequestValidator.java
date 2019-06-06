package com.wirecardchallenge.rest.controller.card.validator;

import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import com.wirecardchallenge.rest.controller.exception.card.CardInvalidDataHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Slf4j
public class CardRequestValidator implements Validator {

    private final static String DEFAULT_INIT_YEAR = "20";

    @Override
    public boolean supports(Class<?> aClass) {
        return CardRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        log.info("Card Validator in action !!");
        CardRequest cardRequest = (CardRequest) o;
        buildExpirationDate(cardRequest.getExpirationDate());
        log.info("Card Name is " + cardRequest.getName());
    }

    private void buildExpirationDate(String strDate)
        throws CardInvalidDataHttpException {
        LocalDate localDate;
        if (strDate.length() != 5) {
            log.warn("Invalid Expiration Date length !!");
            throw new CardInvalidDataHttpException("Invalid Expiration Date length !!");
        }
        try{
            String month = strDate.substring(0,2);
            String year = DEFAULT_INIT_YEAR + strDate.substring(3,5);
            localDate = LocalDate.of(Integer.valueOf(year),
                Integer.valueOf(month), lastDayOfMonth(Integer.valueOf(month),
                    Integer.valueOf(year)));
        }catch (Exception e){
            log.warn("Invalid Expiration Date Format (MM/yy) !!");
            throw new CardInvalidDataHttpException("Invalid Expiration Date Format (MM/yy) !!");
        }
        if (localDate.isBefore(LocalDate.now())) {
            log.warn("Invalid Expiration Date !! Past Date !!");
            throw new CardInvalidDataHttpException("Invalid Expiration Date !! Past Date !!");
        }
    }

    private Integer lastDayOfMonth(Integer month, Integer year){
        LocalDate initial = LocalDate.of(year, month,1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        return Integer.valueOf(end.toString().substring(8,10));
    }
}
