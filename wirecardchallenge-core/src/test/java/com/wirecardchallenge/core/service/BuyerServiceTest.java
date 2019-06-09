package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.BuyerEntity;
import com.wirecardchallenge.core.entity.ClientEntity;
import com.wirecardchallenge.core.repository.BuyerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class BuyerServiceTest {

    @Mock
    BuyerRepository buyerRepository;

    @InjectMocks
    @Spy
    BuyerService buyerService;

    private static final Long BUYER_ID_1 = 1l;
    private static final UUID BUYER_PUBLIC_ID_1 = UUID.randomUUID();
    private static final String BUYER_NAME_1 = "José da Silva";
    private static final String BUYER_EMAIL_1 = "jose.silva@wirecard.com";
    private static final String BUYER_CPF_1 = "331.406.850-68";
    private static final LocalDateTime BUYER_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long BUYER_ID_2 = 2l;
    private static final UUID BUYER_PUBLIC_ID_2 = UUID.randomUUID();
    private static final String BUYER_NAME_2 = "João Santos";
    private static final String BUYER_EMAIL_2 = "joao.santos@wirecard.com";
    private static final String BUYER_CPF_2 = "718.677.760-06";
    private static final LocalDateTime BUYER_CREATED_AT_2 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_2 = LocalDateTime.now().plusMinutes(10);

    private static final Long BUYER_ID_3 = 3l;
    private static final UUID BUYER_PUBLIC_ID_3 = UUID.randomUUID();
    private static final String BUYER_NAME_3 = "Carlos Alberto";
    private static final String BUYER_EMAIL_3 = "carlos.alberto@wirecard.com";
    private static final String BUYER_CPF_3 = "415.850.460-00";
    private static final LocalDateTime BUYER_CREATED_AT_3 = LocalDateTime.now();
    private static final LocalDateTime BUYER_UPDATED_AT_3 = LocalDateTime.now().plusMinutes(10);

    private static final Long CLIENT_ID_1 = 1L;
    private static final UUID CLIENT_PUBLIC_ID_1 = UUID.randomUUID();
    private static final LocalDateTime CLIENT_CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime CLIENT_UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {
        
        List<BuyerEntity> buyerEntities = buildBuyerEntities();
        Page<BuyerEntity> buyerEntitiesPage = new PageImpl<>(buyerEntities);

        when(buyerRepository.findAll(any(Pageable.class)))
            .thenReturn(buyerEntitiesPage);
        Page<BuyerDto> buyerDtos = buyerService.findAll(PageRequest.of(1, 1));

        assertNotNull(buyerDtos);

        assertEquals(3, buyerDtos.getContent().size());
        assertEquals(BUYER_PUBLIC_ID_1, buyerDtos.getContent().get(0).getPublicId());
        assertEquals(BUYER_NAME_1,buyerDtos.getContent().get(0).getName());
        assertEquals(BUYER_EMAIL_1,buyerDtos.getContent().get(0).getEmail());
        assertEquals(BUYER_CPF_1, buyerDtos.getContent().get(0).getCpf());
        assertEquals(BUYER_CREATED_AT_1, buyerDtos.getContent().get(0).getCreatedAt());
        assertEquals(BUYER_UPDATED_AT_1, buyerDtos.getContent().get(0).getUpdatedAt());
        assertEquals(CLIENT_PUBLIC_ID_1, buyerDtos.getContent().get(0).getClientDto().getPublicId());

        assertEquals(BUYER_PUBLIC_ID_2, buyerDtos.getContent().get(1).getPublicId());
        assertEquals(BUYER_NAME_2,buyerDtos.getContent().get(1).getName());
        assertEquals(BUYER_EMAIL_2,buyerDtos.getContent().get(1).getEmail());
        assertEquals(BUYER_CPF_2, buyerDtos.getContent().get(1).getCpf());
        assertEquals(BUYER_CREATED_AT_2, buyerDtos.getContent().get(1).getCreatedAt());
        assertEquals(BUYER_UPDATED_AT_2, buyerDtos.getContent().get(1).getUpdatedAt());
        assertEquals(CLIENT_PUBLIC_ID_1, buyerDtos.getContent().get(1).getClientDto().getPublicId());

        assertEquals(BUYER_PUBLIC_ID_3, buyerDtos.getContent().get(2).getPublicId());
        assertEquals(BUYER_NAME_3,buyerDtos.getContent().get(2).getName());
        assertEquals(BUYER_EMAIL_3,buyerDtos.getContent().get(2).getEmail());
        assertEquals(BUYER_CPF_3, buyerDtos.getContent().get(2).getCpf());
        assertEquals(BUYER_CREATED_AT_3, buyerDtos.getContent().get(2).getCreatedAt());
        assertEquals(BUYER_UPDATED_AT_3, buyerDtos.getContent().get(2).getUpdatedAt());
        assertEquals(CLIENT_PUBLIC_ID_1, buyerDtos.getContent().get(2).getClientDto().getPublicId());
    }

    @Test
    public void findById() {
    }

    @Test
    public void findByPublicId() {
    }

    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    private List<BuyerEntity> buildBuyerEntities(){

        List<BuyerEntity> buyerEntities = new ArrayList<>();

        buyerEntities.add(BuyerEntity.builder()
            .id(BUYER_ID_1)
            .publicId(BUYER_PUBLIC_ID_1)
            .name(BUYER_NAME_1)
            .email(BUYER_EMAIL_1)
            .cpf(BUYER_CPF_1)
            .client(buildClientEntity())
            .createdAt(BUYER_CREATED_AT_1)
            .updatedAt(BUYER_UPDATED_AT_1)
            .build());

        buyerEntities.add(BuyerEntity.builder()
            .id(BUYER_ID_2)
            .publicId(BUYER_PUBLIC_ID_2)
            .name(BUYER_NAME_2)
            .email(BUYER_EMAIL_2)
            .cpf(BUYER_CPF_2)
            .client(buildClientEntity())
            .createdAt(BUYER_CREATED_AT_2)
            .updatedAt(BUYER_UPDATED_AT_2)
            .build());

        buyerEntities.add(BuyerEntity.builder()
            .id(BUYER_ID_3)
            .publicId(BUYER_PUBLIC_ID_3)
            .name(BUYER_NAME_3)
            .email(BUYER_EMAIL_3)
            .cpf(BUYER_CPF_3)
            .client(buildClientEntity())
            .createdAt(BUYER_CREATED_AT_3)
            .updatedAt(BUYER_UPDATED_AT_3)
            .build());

        return buyerEntities;
    }

    private ClientEntity buildClientEntity(){
        return ClientEntity.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLIC_ID_1)
            .createdAt(CLIENT_CREATED_AT_1)
            .updatedAt(CLIENT_UPDATED_AT_1)
            .build();
    }
}
