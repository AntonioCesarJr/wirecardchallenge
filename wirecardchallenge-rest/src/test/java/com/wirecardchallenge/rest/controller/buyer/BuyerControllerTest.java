package com.wirecardchallenge.rest.controller.buyer;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.service.BuyerService;
import org.hibernate.validator.constraints.EAN;
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
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class BuyerControllerTest {

    private static final Long BUYER_ID_1 = 1L;
    private static final UUID BUYER_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String BUYER_NAME_1 = "João Da Silva";
    private static final String BUYER_EMAIL_1 = "joao.silva@wirecard.com";
    private static final String BUYER_CPF_1 = "064.423.070-34";
    private static final LocalDateTime BUYER_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long BUYER_ID_2 = 2L;
    private static final UUID BUYER_PUBLIC_ID_2 = UUID.randomUUID();
    private static final String BUYER_NAME_2 = "José Da Silva";
    private static final String BUYER_EMAIL_2 = "jose.silva@wirecard.com";
    private static final String BUYER_CPF_2 = "274.244.210-34";
    private static final LocalDateTime BUYER_CREATED_AT_2 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_2 = LocalDateTime.now().plusMinutes(10);

    private static final Long BUYER_ID_3 = 3L;
    private static final UUID BUYER_PUBLIC_ID_3 = UUID.randomUUID();
    private static final String BUYER_NAME_3 = "Carlos Da Silva";
    private static final String BUYER_EMAIL_3 = "carlos.silva@wirecard.com";
    private static final String BUYER_CPF_3 = "305.919.900-08";
    private static final LocalDateTime BUYER_CREATED_AT_3 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_3 = LocalDateTime.now().plusMinutes(10);

    private static final Long CLIENT_ID_1 = 1L;
    private static final UUID CLIENT_PUBLIC_ID_1 = UUID.randomUUID();
    private static final LocalDateTime CLIENT_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CLIENT_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    @Mock
    BuyerService buyerService;

    @InjectMocks
    BuyerController buyerController;

    @Before
    public void setUp() {MockitoAnnotations.initMocks(this);}

    @Test
    public void findAll() {

        List<BuyerDto> buyerDto = buildBuyerDtoList();
        Page<BuyerDto> buyerDtoPage = new PageImpl<>(buyerDto);

        when(buyerService.findAll(any(Pageable.class)))
            .thenReturn(buyerDtoPage);

        ResponseEntity<Page<BuyerDto>> responseEntity =
            buyerController.findAll(PageRequest.of(1, 1));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(3, responseEntity.getBody().getContent().size());
    }

    @Test
    public void findByPublicId() {
    }

    @Test
    public void add() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }


    private List<BuyerDto> buildBuyerDtoList(){
        List<BuyerDto> buyerDtos = new ArrayList<>();
        buyerDtos.add(buildBuyerDto(BUYER_ID_1, BUYER_PUBLIC_ID_1, BUYER_NAME_1, BUYER_EMAIL_1,
            BUYER_CPF_1, BUYER_CREATED_AT_1,BUYER_UPDATED_AT_1));
        buyerDtos.add(buildBuyerDto(BUYER_ID_2, BUYER_PUBLIC_ID_2, BUYER_NAME_2, BUYER_EMAIL_2,
            BUYER_CPF_2, BUYER_CREATED_AT_2, BUYER_UPDATED_AT_2));
        buyerDtos.add(buildBuyerDto(BUYER_ID_3, BUYER_PUBLIC_ID_3, BUYER_NAME_3, BUYER_EMAIL_3,
            BUYER_CPF_3, BUYER_CREATED_AT_3,BUYER_UPDATED_AT_3));
        return buyerDtos;
    }

    private BuyerDto buildBuyerDto(Long id, UUID publicId, String name,String email, String cpf,
                                   LocalDateTime createdAt, LocalDateTime  updatedAt){
        return BuyerDto.builder()
            .id(id)
            .publicId(publicId)
            .name(name)
            .email(email)
            .cpf(cpf)
            .clientDto(buildClientDto())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

    private ClientDto buildClientDto() {
        return ClientDto.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLIC_ID_1)
            .createdAt(CLIENT_CREATED_AT_1)
            .updatedAt(CLIENT_UPDATED_AT_1)
            .build();
    }
}
