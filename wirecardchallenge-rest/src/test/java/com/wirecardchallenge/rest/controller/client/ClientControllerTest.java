package com.wirecardchallenge.rest.controller.client;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.service.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
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
public class ClientControllerTest {

    private static final Long CLIENT_ID_1 = 1L;
    private static final UUID CLIENT_PUBLICID_1 = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long CLIENT_ID_2 = 2L;
    private static final UUID CLIENT_PUBLICID_2 = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT_2 = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT_2 = LocalDateTime.now().plusMinutes(15);

    private static final Long CLIENT_ID_3 = 3L;
    private static final UUID CLIENT_PUBLICID_3 = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT_3 = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT_3 = LocalDateTime.now().plusMinutes(20);


    @InjectMocks
    ClientController clientController;

    @Mock
    ClientService clientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {
        List<ClientDto> clientDtos = buildClientDtoList();
        Page<ClientDto> clientDtoPage = new PageImpl<>(clientDtos);
        when(clientService.findAll(any(Pageable.class)))
            .thenReturn(clientDtoPage);
        ResponseEntity<Page<ClientDto>> clientPageResponse =
            clientController.findAll(PageRequest.of(1, 1));
        assertNotNull(clientPageResponse);
    }

    @Test
    public void findByPublicId() {
    }

    @Test
    public void add() {
        when(clientService.create()).thenReturn(ClientDto.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLICID_1)
            .createdAt(CREATED_AT_1)
            .updatedAt(UPDATED_AT_1)
            .build());
        ResponseEntity<ClientDto> clientDtoResponseEntity = clientController.add();
        assertEquals(HttpStatus.OK, clientDtoResponseEntity.getStatusCode());
        assertEquals(CLIENT_ID_1, clientDtoResponseEntity.getBody().getId());
        assertEquals(CLIENT_PUBLICID_1, clientDtoResponseEntity.getBody().getPublicId());
        assertEquals(CREATED_AT_1, clientDtoResponseEntity.getBody().getCreatedAt());
        assertEquals(UPDATED_AT_1, clientDtoResponseEntity.getBody().getUpdatedAt());
    }

    @Test
    public void delete() {
    }

    private List<ClientDto> buildClientDtoList(){
        List<ClientDto> clientDtoList = new ArrayList<>();
        clientDtoList.add(ClientDto.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLICID_1)
            .createdAt(CREATED_AT_1)
            .updatedAt(UPDATED_AT_1)
            .build());
        clientDtoList.add(ClientDto.builder()
            .id(CLIENT_ID_2)
            .publicId(CLIENT_PUBLICID_2)
            .createdAt(CREATED_AT_2)
            .updatedAt(UPDATED_AT_2)
            .build());
        clientDtoList.add(ClientDto.builder()
            .id(CLIENT_ID_3)
            .publicId(CLIENT_PUBLICID_3)
            .createdAt(CREATED_AT_3)
            .updatedAt(UPDATED_AT_3)
            .build());
        return clientDtoList;
    }
}
