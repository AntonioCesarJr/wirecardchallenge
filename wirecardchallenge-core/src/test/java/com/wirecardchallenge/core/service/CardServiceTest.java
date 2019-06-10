package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.CardDto;
import com.wirecardchallenge.core.entity.BuyerEntity;
import com.wirecardchallenge.core.entity.CardEntity;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.card.CardNotFoundException;
import com.wirecardchallenge.core.repository.BuyerRepository;
import com.wirecardchallenge.core.repository.CardRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CardServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    BuyerRepository buyerRepository;

    @InjectMocks
    CardService cardService;

    private static final Long CARD_ID_1 = 1L;
    private static final UUID CARD_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String CARD_NAME_1 = "RAIMUNDO PEREIRA";
    private static final String CARD_NUMBER_1 = "1111222233334444";
    private static final String CARD_EXPIRATION_DATE_1 = "02/21";
    private static final String CARD_CVV_1 = "123";
    private static final LocalDateTime CARD_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CARD_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long CARD_ID_2 = 2L;
    private static final UUID CARD_PUBLIC_ID_2 = UUID.randomUUID();
    private static final String CARD_NAME_2 = "APARECIDO PEREIRA";
    private static final String CARD_NUMBER_2 = "4140623642653952";
    private static final String CARD_EXPIRATION_DATE_2 = "02/23";
    private static final String CARD_CVV_2 = "321";
    private static final LocalDateTime CARD_CREATED_AT_2 = LocalDateTime.now().plusMinutes(20);
    private static final LocalDateTime CARD_UPDATED_AT_2 = LocalDateTime.now().plusMinutes(30);

    private static final Long CARD_ID_3 = 3L;
    private static final UUID CARD_PUBLIC_ID_3 = UUID.randomUUID();
    private static final String CARD_NAME_3 = "AFONSO PEREIRA";
    private static final String CARD_NUMBER_3 = "4143728206250074";
    private static final String CARD_EXPIRATION_DATE_3 = "10/22";
    private static final String CARD_CVV_3 = "988";
    private static final LocalDateTime CARD_CREATED_AT_3 = LocalDateTime.now().plusMinutes(40);
    private static final LocalDateTime CARD_UPDATED_AT_3 = LocalDateTime.now().plusMinutes(50);

    private static final Long BUYER_ID_1 = 4L;
    private static final UUID BUYER_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String BUYER_NAME_1 = "José da Silva";
    private static final String BUYER_EMAIL_1 = "jose.silva@wirecard.com";
    private static final String BUYER_CPF_1 = "331.406.850-68";
    private static final LocalDateTime BUYER_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(60);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {

        List<CardEntity> cardEntities = buildCardEntities();
        Page<CardEntity> cardEntityPage = new PageImpl<>(cardEntities);

        when(cardRepository.findAll(any(Pageable.class)))
            .thenReturn(cardEntityPage);

        Page<CardDto> cardDtos = cardService.findAll(PageRequest.of(1, 1));

        assertNotNull(cardDtos);
        assertEquals(3, cardDtos.getContent().size());
        assertEquals(CARD_PUBLIC_ID_1, cardDtos.getContent().get(0).getPublicId());
        assertEquals(CARD_NAME_1, cardDtos.getContent().get(0).getName());
        assertEquals(CARD_NUMBER_1, cardDtos.getContent().get(0).getNumber());
        assertEquals(CARD_EXPIRATION_DATE_1, cardDtos.getContent().get(0).getExpirationDate());
        assertEquals(CARD_CVV_1, cardDtos.getContent().get(0).getCVV());
        assertEquals(BUYER_PUBLIC_ID_1, cardDtos.getContent().get(0).getBuyerDto().getPublicId());
        assertEquals(BUYER_NAME_1, cardDtos.getContent().get(0).getBuyerDto().getName());
        assertEquals(BUYER_EMAIL_1, cardDtos.getContent().get(0).getBuyerDto().getEmail());
        assertEquals(BUYER_CPF_1, cardDtos.getContent().get(0).getBuyerDto().getCpf());
        assertEquals(CARD_CREATED_AT_1, cardDtos.getContent().get(0).getCreatedAt());
        assertEquals(CARD_UPDATED_AT_1, cardDtos.getContent().get(0).getUpdatedAt());

        assertEquals(3, cardDtos.getContent().size());
        assertEquals(CARD_PUBLIC_ID_2, cardDtos.getContent().get(1).getPublicId());
        assertEquals(CARD_NAME_2, cardDtos.getContent().get(1).getName());
        assertEquals(CARD_NUMBER_2, cardDtos.getContent().get(1).getNumber());
        assertEquals(CARD_EXPIRATION_DATE_2, cardDtos.getContent().get(1).getExpirationDate());
        assertEquals(CARD_CVV_2, cardDtos.getContent().get(1).getCVV());
        assertEquals(BUYER_PUBLIC_ID_1, cardDtos.getContent().get(1).getBuyerDto().getPublicId());
        assertEquals(BUYER_NAME_1, cardDtos.getContent().get(1).getBuyerDto().getName());
        assertEquals(BUYER_EMAIL_1, cardDtos.getContent().get(1).getBuyerDto().getEmail());
        assertEquals(BUYER_CPF_1, cardDtos.getContent().get(0).getBuyerDto().getCpf());
        assertEquals(CARD_CREATED_AT_2, cardDtos.getContent().get(1).getCreatedAt());
        assertEquals(CARD_UPDATED_AT_2, cardDtos.getContent().get(1).getUpdatedAt());

        assertEquals(3, cardDtos.getContent().size());
        assertEquals(CARD_PUBLIC_ID_3, cardDtos.getContent().get(2).getPublicId());
        assertEquals(CARD_NAME_3, cardDtos.getContent().get(2).getName());
        assertEquals(CARD_NUMBER_3, cardDtos.getContent().get(2).getNumber());
        assertEquals(CARD_EXPIRATION_DATE_3, cardDtos.getContent().get(2).getExpirationDate());
        assertEquals(CARD_CVV_3, cardDtos.getContent().get(2).getCVV());
        assertEquals(BUYER_PUBLIC_ID_1, cardDtos.getContent().get(2).getBuyerDto().getPublicId());
        assertEquals(BUYER_NAME_1, cardDtos.getContent().get(2).getBuyerDto().getName());
        assertEquals(BUYER_EMAIL_1, cardDtos.getContent().get(2).getBuyerDto().getEmail());
        assertEquals(BUYER_CPF_1, cardDtos.getContent().get(0).getBuyerDto().getCpf());
        assertEquals(CARD_CREATED_AT_3, cardDtos.getContent().get(2).getCreatedAt());
        assertEquals(CARD_UPDATED_AT_3, cardDtos.getContent().get(2).getUpdatedAt());
    }

    @Test
    public void findByPublicId() throws CardNotFoundException {

        when(cardRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.of(buildCardEntity()));

        CardDto cardDto = cardService.findByPublicId(CARD_PUBLIC_ID_1);

        assertNotNull(cardDto);
        assertEquals(CARD_PUBLIC_ID_1, cardDto.getPublicId());
        assertEquals(CARD_NAME_1, cardDto.getName());
        assertEquals(CARD_NUMBER_1, cardDto.getNumber());
        assertEquals(CARD_EXPIRATION_DATE_1, cardDto.getExpirationDate());
        assertEquals(CARD_CVV_1, cardDto.getCVV());
        assertEquals(BUYER_PUBLIC_ID_1, cardDto.getBuyerDto().getPublicId());
        assertEquals(BUYER_NAME_1, cardDto.getBuyerDto().getName());
        assertEquals(BUYER_EMAIL_1, cardDto.getBuyerDto().getEmail());
        assertEquals(BUYER_CPF_1, cardDto.getBuyerDto().getCpf());
        assertEquals(CARD_CREATED_AT_1, cardDto.getCreatedAt());
        assertEquals(CARD_UPDATED_AT_1, cardDto.getUpdatedAt());
    }

    @Test(expected = CardNotFoundException.class)
    public void findByPublicIdCardNotFoundException() throws CardNotFoundException {

        when(cardRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.empty());

        cardService.findByPublicId(CARD_PUBLIC_ID_1);
    }

    @Test
    public void create() throws BuyerNotFoundException {

        when(buyerRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.of(buildBuyerEntity()));
        when(cardRepository.save(any(CardEntity.class)))
            .thenReturn(buildCardEntity());

        CardDto cardDto = cardService.create(buildCardDto());

        assertNotNull(cardDto);
        assertEquals(CARD_PUBLIC_ID_1, cardDto.getPublicId());
        assertEquals(CARD_NAME_1, cardDto.getName());
        assertEquals(CARD_NUMBER_1, cardDto.getNumber());
        assertEquals(CARD_EXPIRATION_DATE_1, cardDto.getExpirationDate());
        assertEquals(CARD_CVV_1, cardDto.getCVV());
        assertEquals(BUYER_PUBLIC_ID_1,cardDto.getBuyerDto().getPublicId());
        assertEquals(BUYER_NAME_1,cardDto.getBuyerDto().getName());
        assertEquals(BUYER_EMAIL_1,cardDto.getBuyerDto().getEmail());
        assertEquals(BUYER_CPF_1,cardDto.getBuyerDto().getCpf());
        assertEquals(CARD_CREATED_AT_1, cardDto.getCreatedAt());
        assertEquals(CARD_UPDATED_AT_1, cardDto.getUpdatedAt());
    }

    @Test(expected = BuyerNotFoundException.class)
    public void createBuyerNotFoundException() throws BuyerNotFoundException {

        when(buyerRepository.findByPublicId(any(UUID.class)))
            .thenReturn(Optional.empty());
        cardService.create(buildCardDto());
    }

    @Test
    public void update() throws CardNotFoundException, BuyerNotFoundException {


        when(buyerRepository.findByPublicId(buildCardDto().getBuyerDto().getPublicId()))
            .thenReturn(Optional.of(buildBuyerEntity()));
        when(cardRepository.findByPublicId(buildCardDto().getPublicId()))
            .thenReturn(Optional.of(buildCardEntity()));
        when(cardRepository.save(any(CardEntity.class)))
            .thenReturn(buildCardEntity());

        CardDto cardDtoResponse = cardService.update(CARD_PUBLIC_ID_1, buildCardDto());
        assertNotNull(cardDtoResponse);
    }

    @Test(expected = BuyerNotFoundException.class)
    public void updateBuyerNotFoundException() throws CardNotFoundException, BuyerNotFoundException {

        when(buyerRepository.findByPublicId(buildCardDto().getBuyerDto().getPublicId()))
            .thenReturn(Optional.empty());

        cardService.update(CARD_PUBLIC_ID_1, buildCardDto());
    }

    @Test(expected = CardNotFoundException.class)
    public void updateCardNotFoundException() throws CardNotFoundException, BuyerNotFoundException {

        when(buyerRepository.findByPublicId(buildCardDto().getBuyerDto().getPublicId()))
            .thenReturn(Optional.of(buildBuyerEntity()));
        when(cardRepository.findByPublicId(buildCardDto().getPublicId()))
            .thenReturn(Optional.empty());

        cardService.update(CARD_PUBLIC_ID_1, buildCardDto());
    }

    @Test
    public void delete() throws CardNotFoundException {

        int invocations = 1;

        when(cardRepository.findByPublicId(CARD_PUBLIC_ID_1))
            .thenReturn(Optional.of(buildCardEntity()));
        doNothing().when(cardRepository).delete(any(CardEntity.class));

        cardService.delete(CARD_PUBLIC_ID_1);
        verify(cardRepository, times(invocations)).delete(buildCardEntity());
    }

    @Test(expected = CardNotFoundException.class)
    public void deleteCardNotFoundException() throws CardNotFoundException {

        when(cardRepository.findByPublicId(CARD_PUBLIC_ID_1))
            .thenReturn(Optional.empty());
        doNothing().when(cardRepository).delete(any(CardEntity.class));

        cardService.delete(CARD_PUBLIC_ID_1);
    }

    private CardEntity buildCardEntity(){
        return CardEntity.builder()
            .id(CARD_ID_1)
            .publicId(CARD_PUBLIC_ID_1)
            .name(CARD_NAME_1)
            .number(CARD_NUMBER_1)
            .expirationDate(CARD_EXPIRATION_DATE_1)
            .CVV(CARD_CVV_1)
            .buyer(buildBuyerEntity())
            .createdAt(CARD_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build();
    }

    private List<CardEntity> buildCardEntities(){
        List<CardEntity> cardEntities = new ArrayList<>();

        cardEntities.add(CardEntity.builder()
            .id(CARD_ID_1)
            .publicId(CARD_PUBLIC_ID_1)
            .name(CARD_NAME_1)
            .number(CARD_NUMBER_1)
            .expirationDate(CARD_EXPIRATION_DATE_1)
            .CVV(CARD_CVV_1)
            .buyer(buildBuyerEntity())
            .createdAt(CARD_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build());

        cardEntities.add(CardEntity.builder()
            .id(CARD_ID_2)
            .publicId(CARD_PUBLIC_ID_2)
            .name(CARD_NAME_2)
            .number(CARD_NUMBER_2)
            .expirationDate(CARD_EXPIRATION_DATE_2)
            .CVV(CARD_CVV_2)
            .buyer(buildBuyerEntity())
            .createdAt(CARD_CREATED_AT_2)
            .updatedAt(CARD_UPDATED_AT_2)
            .build());

        cardEntities.add(CardEntity.builder()
            .id(CARD_ID_3)
            .publicId(CARD_PUBLIC_ID_3)
            .name(CARD_NAME_3)
            .number(CARD_NUMBER_3)
            .expirationDate(CARD_EXPIRATION_DATE_3)
            .CVV(CARD_CVV_3)
            .buyer(buildBuyerEntity())
            .createdAt(CARD_CREATED_AT_3)
            .updatedAt(CARD_UPDATED_AT_3)
            .build());

        return cardEntities;
    }

    private BuyerEntity buildBuyerEntity(){
        return BuyerEntity.builder()
            .id(BUYER_ID_1)
            .publicId(BUYER_PUBLIC_ID_1)
            .name(BUYER_NAME_1)
            .email(BUYER_EMAIL_1)
            .cpf(BUYER_CPF_1)
            .createdAt(BUYER_CREATED_AT_1)
            .updatedAt(BUYER_UPDATED_AT_1)
            .build();
    }

    private CardDto buildCardDto() {
        return CardDto.builder()
            .id(CARD_ID_1)
            .publicId(CARD_PUBLIC_ID_1)
            .name(CARD_NAME_1)
            .number(CARD_NUMBER_1)
            .expirationDate(CARD_EXPIRATION_DATE_1)
            .CVV(CARD_CVV_1)
            .buyerDto(buildBuyerDto())
            .createdAt(CARD_CREATED_AT_1)
            .updatedAt(CARD_UPDATED_AT_1)
            .build();

    }

    private BuyerDto buildBuyerDto() {
        return BuyerDto.builder()
            .id(BUYER_ID_1)
            .publicId(BUYER_PUBLIC_ID_1)
            .name(BUYER_NAME_1)
            .email(BUYER_EMAIL_1)
            .cpf(BUYER_CPF_1)
            .createdAt(BUYER_CREATED_AT_1)
            .updatedAt(BUYER_UPDATED_AT_1)
            .build();
    }
}
