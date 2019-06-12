package com.wirecardchallenge.rest.controller.card;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.service.CardService;
import com.wirecardchallenge.rest.controller.card.request.CardRequest;
import com.wirecardchallenge.rest.exception.buyer.BuyerNotFoundHttpException;
import com.wirecardchallenge.rest.exception.card.CardNotFoundHttpException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CardControllerTest {

    private static final Long CARD_ID_1 = 1L;
    private static final UUID CARD_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String CARD_NAME_1 = "João Da Silva";
    private static final String CARD_NUMBER_1 = "1111222233334444";
    private static final String CARD_EXPIRATION_DATE_1 = "10/22";
    private static final String CARD_CVV_1 = "111";
    private static final LocalDateTime CARD_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CARD_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long CARD_ID_2 = 2L;
    private static final UUID CARD_PUBLIC_ID_2 = UUID.randomUUID();
    private static final String CARD_NAME_2 = "José Da Silva";
    private static final String CARD_NUMBER_2 = "4115106154271877";
    private static final String CARD_EXPIRATION_DATE_2 = "11/23";
    private static final String CARD_CVV_2 = "111";
    private static final LocalDateTime CARD_CREATED_AT_2 = LocalDateTime.now();
    private static final LocalDateTime CARD_UPDATED_AT_2 = LocalDateTime.now().plusMinutes(10);

    private static final Long CARD_ID_3 = 3L;
    private static final UUID CARD_PUBLIC_ID_3 = UUID.randomUUID();
    private static final String CARD_NAME_3 = "Carlos Da Silva";
    private static final String CARD_NUMBER_3 = "4066695984520056";
    private static final String CARD_EXPIRATION_DATE_3 = "07/24";
    private static final String CARD_CVV_3 = "111";
    private static final LocalDateTime CARD_CREATED_AT_3 = LocalDateTime.now();
    private static final LocalDateTime CARD_UPDATED_AT_3 = LocalDateTime.now().plusMinutes(10);

    private static final UUID BUYER_PUBLIC_ID_1 = UUID.randomUUID();

    private static final String NOT_FOUND = "RESOURCE NOT FOUND";
    private static final String INTEGRITY_CONSTRAINT = "INTEGRITY CONSTRAINT FAIL";

    @Mock
    CardService cardService;

    @InjectMocks
    CardController cardController;

    @Before
    public void setUp() {MockitoAnnotations.initMocks(this);}

    @Test
    public void findAll() {

        List<CardDto> cardDtos = buildCardDtoList();
        Page<CardDto> cardDtosPage = new PageImpl<>(cardDtos);

        when(cardService.findAll(any(Pageable.class)))
            .thenReturn(cardDtosPage);

        ResponseEntity<Page<CardDto>> cardResponse = cardController.findAll(PageRequest.of(1, 1));

        assertNotNull(cardResponse);
        assertEquals(3, cardResponse.getBody().getContent().size());
    }

    @Test
    public void findByPublicId() throws CardNotFoundException {

        when(cardService.findByPublicId(any(UUID.class)))
            .thenReturn(buildCardDto(CARD_ID_1,CARD_PUBLIC_ID_1,CARD_NAME_1,CARD_NUMBER_1,CARD_EXPIRATION_DATE_1,
                CARD_CVV_1,CARD_CREATED_AT_1,CARD_UPDATED_AT_1));

        ResponseEntity<CardDto> cardDtoResponse = cardController.findByPublicId(CARD_PUBLIC_ID_1);
        assertNotNull(cardDtoResponse);
    }

    @Test(expected = CardNotFoundHttpException.class)
    public void findByPublicIdCardNotFoundException() throws CardNotFoundException {

        when(cardService.findByPublicId(any(UUID.class)))
            .thenThrow(new CardNotFoundException(NOT_FOUND));

        cardController.findByPublicId(CARD_PUBLIC_ID_1);
    }

    @Test
    public void add() throws BuyerNotFoundException {

        when(cardService.create(any(CardDto.class)))
            .thenReturn(buildCardDto(CARD_ID_1,CARD_PUBLIC_ID_1,CARD_NAME_1,CARD_NUMBER_1,CARD_EXPIRATION_DATE_1,
                CARD_CVV_1,CARD_CREATED_AT_1,CARD_UPDATED_AT_1));

        ResponseEntity<CardDto> cardDtoResponse = cardController.add(buildCardRequest(CARD_NAME_1, CARD_NUMBER_1, CARD_EXPIRATION_DATE_1,
            CARD_CVV_1, BUYER_PUBLIC_ID_1));

        assertNotNull(cardDtoResponse);
    }

    @Test(expected = BuyerNotFoundHttpException.class)
    public void addBuyerNotFoundException() throws BuyerNotFoundException {

        when(cardService.create(any(CardDto.class)))
            .thenThrow(new BuyerNotFoundException(NOT_FOUND));

        cardController.add(buildCardRequest(CARD_NAME_1, CARD_NUMBER_1, CARD_EXPIRATION_DATE_1,
            CARD_CVV_1, BUYER_PUBLIC_ID_1));
    }

    @Test
    public void update() throws CardNotFoundException, BuyerNotFoundException {

        when(cardService.update(any(UUID.class), any(CardDto.class)))
            .thenReturn(buildCardDto(CARD_ID_1,CARD_PUBLIC_ID_1,CARD_NAME_1,CARD_NUMBER_1,CARD_EXPIRATION_DATE_1,
                CARD_CVV_1,CARD_CREATED_AT_1,CARD_UPDATED_AT_1));

        ResponseEntity<CardDto> cardDtoResponse = cardController.update(buildCardRequest(CARD_NAME_1, CARD_NUMBER_1,
            CARD_EXPIRATION_DATE_1, CARD_CVV_1, BUYER_PUBLIC_ID_1), CARD_PUBLIC_ID_1);

        assertNotNull(cardDtoResponse);
    }

    @Test(expected = BuyerNotFoundHttpException.class)
    public void updateBuyerNotFoundException() throws CardNotFoundException, BuyerNotFoundException {

        when(cardService.update(any(UUID.class), any(CardDto.class)))
            .thenThrow(new BuyerNotFoundException(NOT_FOUND));

        cardController.update(buildCardRequest(CARD_NAME_1, CARD_NUMBER_1,
            CARD_EXPIRATION_DATE_1, CARD_CVV_1, BUYER_PUBLIC_ID_1), CARD_PUBLIC_ID_1);
    }

    @Test(expected = CardNotFoundHttpException.class)
    public void updateCardNotFoundException() throws CardNotFoundException, BuyerNotFoundException {

        when(cardService.update(any(UUID.class), any(CardDto.class)))
            .thenThrow(new CardNotFoundException(NOT_FOUND));

        cardController.update(buildCardRequest(CARD_NAME_1, CARD_NUMBER_1,
            CARD_EXPIRATION_DATE_1, CARD_CVV_1, BUYER_PUBLIC_ID_1), CARD_PUBLIC_ID_1);
    }

    @Test
    public void delete() throws CardNotFoundException {

        doNothing().when(cardService).delete(any(UUID.class));
        ResponseEntity<CardDto> cardDtoResponse = cardController.delete(CARD_PUBLIC_ID_1);
        assertNotNull(cardDtoResponse);
        assertEquals(HttpStatus.NO_CONTENT, cardDtoResponse.getStatusCode());
    }

    @Test(expected = CardNotFoundHttpException.class)
    public void deleteCardNotFoundException() throws CardNotFoundException {

        doThrow(new CardNotFoundException(NOT_FOUND))
            .when(cardService).delete(any(UUID.class));
        cardController.delete(CARD_PUBLIC_ID_1);
    }

    private List<CardDto> buildCardDtoList(){
        List<CardDto> cardEntities = new ArrayList<>();
        cardEntities.add(buildCardDto(CARD_ID_1,CARD_PUBLIC_ID_1,CARD_NAME_1,CARD_NUMBER_1,CARD_EXPIRATION_DATE_1,
            CARD_CVV_1,CARD_CREATED_AT_1,CARD_UPDATED_AT_1));
        cardEntities.add(buildCardDto(CARD_ID_2,CARD_PUBLIC_ID_2,CARD_NAME_2,CARD_NUMBER_2,CARD_EXPIRATION_DATE_2,
            CARD_CVV_2,CARD_CREATED_AT_2,CARD_UPDATED_AT_2));
        cardEntities.add(buildCardDto(CARD_ID_3,CARD_PUBLIC_ID_3,CARD_NAME_3,CARD_NUMBER_3,CARD_EXPIRATION_DATE_3,
            CARD_CVV_3,CARD_CREATED_AT_3,CARD_UPDATED_AT_3));
        return cardEntities;
    }

    private CardDto buildCardDto(Long id, UUID publicId, String name, String number,String expirationDate,
                                       String CVV, LocalDateTime createdAt, LocalDateTime updatedAt){
        return CardDto.builder()
            .id(id)
            .publicId(publicId)
            .name(name)
            .number(number)
            .expirationDate(expirationDate)
            .CVV(CVV)
            .buyerDto(buildBuyerDto())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

    private BuyerDto buildBuyerDto() {
        return BuyerDto.builder().build();
    }

    private CardRequest buildCardRequest(String name, String number,String expirationDate,
                                         String CVV, UUID buyerPublicId){
        return CardRequest.builder()
            .name(name)
            .number(number)
            .expirationDate(expirationDate)
            .CVV(CVV)
            .buyerPublicId(buyerPublicId)
            .build();
    }
}

