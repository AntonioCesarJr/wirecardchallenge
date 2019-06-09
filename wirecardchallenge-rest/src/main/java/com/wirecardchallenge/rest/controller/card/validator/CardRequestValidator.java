package com.wirecardchallenge.rest.controller.card.validator;

import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import com.wirecardchallenge.rest.exception.card.CardInvalidDataHttpException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CardRequestValidator implements Validator {

    private final static String DEFAULT_INIT_YEAR = "20";
    private final static String SEPARATOR = "/";

    @Override
    public boolean supports(Class<?> aClass) {
        return CardRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        log.info("CardEntity Validator in action !!");
        CardRequest cardRequest = (CardRequest) o;
        validateExpirationDate(cardRequest.getExpirationDate());
        validateCreditCardNumber(cardRequest.getNumber());
        validateCVV(cardRequest.getCVV());
        log.info("CardEntity Name is " + cardRequest.getName());
    }

    @SuppressFBWarnings("BX_UNBOXING_IMMEDIATELY_REBOXED")
    private void validateExpirationDate(String strDate)
        throws CardInvalidDataHttpException {

        Boolean isBeforeNow = false;
        if (strDate.length() != 5) {
            log.warn("Invalid Expiration Date length !!");
            throw new CardInvalidDataHttpException("Invalid Expiration Date length !!");
        }
        try{
            String year = DEFAULT_INIT_YEAR + strDate.substring(3,5);
            String month = strDate.substring(0,2);
            String strDateToValidate = new StringBuilder()
                .append(lastDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)))
                .append(SEPARATOR)
                .append(month)
                .append(SEPARATOR)
                .append(year)
                .toString();
            final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(strDateToValidate, DATE_FORMAT);
            if (localDate.isBefore(LocalDate.now())) {
                log.warn("Invalid Expiration Date !! Past Date !!");
                isBeforeNow = true;
            }
        }catch (Exception e){
            log.warn("Invalid Expiration Date Format (MM/yy) !!");
            throw new CardInvalidDataHttpException("Invalid Expiration Date Format (MM/yy) !!");
        }
        if (isBeforeNow) throw new CardInvalidDataHttpException("Invalid Expiration Date !! Past Date !!");
    }

    private Integer lastDayOfMonth(Integer month, Integer year){
        LocalDate initial = LocalDate.of(year, month,1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        return Integer.valueOf(end.toString().substring(8,10));
    }

    private void validateCreditCardNumber(String str) {
        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            log.info(str + " is a valid credit cardEntity number");
        } else {
            log.warn(str + " is an invalid credit cardEntity number");
            throw new CardInvalidDataHttpException(str +  " is an invalid credit cardEntity number");
        }
    }

    private void validateCVV(String CVV){
        log.info("I have no idea how to validate CVV !! :(");
        return;
    }
}
