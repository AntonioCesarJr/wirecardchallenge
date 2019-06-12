package com.wirecardchallenge.rest.controller.client;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.service.ClientService;
import com.wirecardchallenge.rest.exception.client.ClientNotFoundHttpException;
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
public class ClientControllerTest {

    @InjectMocks
    ClientController clientController;

    @Mock
    ClientService clientService;

    private static final Long CLIENT_ID_1 = 1L;
    private static final UUID CLIENT_PUBLIC_ID_1 = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT_1 = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT_1 = LocalDateTime.now().plusMinutes(10);

    private static final Long CLIENT_ID_2 = 2L;
    private static final UUID CLIENT_PUBLIC_ID_2 = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT_2 = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT_2 = LocalDateTime.now().plusMinutes(15);

    private static final Long CLIENT_ID_3 = 3L;
    private static final UUID CLIENT_PUBLIC_ID_3 = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT_3 = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT_3 = LocalDateTime.now().plusMinutes(20);
    private static final String DI_EXCEPTION_MESSAGE = "Something wrong is not right !!";

    @Before
    public void setUp() {MockitoAnnotations.initMocks(this);}

    @Test
    public void findAll() {

        List<ClientDto> clientDtos = buildClientDtoList();
        Page<ClientDto> clientDtoPage = new PageImpl<>(clientDtos);

        when(clientService.findAll(any(Pageable.class)))
            .thenReturn(clientDtoPage);

        ResponseEntity<Page<ClientDto>> clientPageResponse =
            clientController.findAll(PageRequest.of(1, 1));

        assertNotNull(clientPageResponse);
        assertEquals(HttpStatus.OK, clientPageResponse.getStatusCode());
        assertEquals(3,clientPageResponse.getBody().getTotalElements());
    }

    @Test
    public void findByPublicId() throws ClientNotFoundException {


        when(clientService.findByPublicId(CLIENT_PUBLIC_ID_1))
            .thenReturn(buildClient_1(CLIENT_ID_1, CLIENT_PUBLIC_ID_1, CREATED_AT_1, UPDATED_AT_1));

        ResponseEntity<ClientDto> clientDtoResponse =
            clientController.findByPublicId(CLIENT_PUBLIC_ID_1);

        assertNotNull(clientDtoResponse);
        assertEquals(HttpStatus.OK, clientDtoResponse.getStatusCode());
        assertEquals(CLIENT_ID_1, clientDtoResponse.getBody().getId());
        assertEquals(CLIENT_PUBLIC_ID_1, clientDtoResponse.getBody().getPublicId());
        assertEquals(CREATED_AT_1, clientDtoResponse.getBody().getCreatedAt());
        assertEquals(UPDATED_AT_1, clientDtoResponse.getBody().getUpdatedAt());
    }

    @Test(expected = ClientNotFoundHttpException.class)
    public void findByPublicIdClientNotFoundException() throws ClientNotFoundException {

        ClientNotFoundException clientNotFoundException = new ClientNotFoundException(DI_EXCEPTION_MESSAGE);
        when(clientService.findByPublicId(CLIENT_PUBLIC_ID_1))
            .thenThrow(clientNotFoundException);

        clientController.findByPublicId(CLIENT_PUBLIC_ID_1);
    }

    @Test
    public void add() {

        when(clientService.create()).thenReturn(buildClient_1(CLIENT_ID_1, CLIENT_PUBLIC_ID_1, CREATED_AT_1, UPDATED_AT_1));

        ResponseEntity<ClientDto> clientDtoResponse = clientController.add();

        assertNotNull(clientDtoResponse);
        assertEquals(HttpStatus.OK, clientDtoResponse.getStatusCode());
        assertEquals(CLIENT_ID_1, clientDtoResponse.getBody().getId());
        assertEquals(CLIENT_PUBLIC_ID_1, clientDtoResponse.getBody().getPublicId());
        assertEquals(CREATED_AT_1, clientDtoResponse.getBody().getCreatedAt());
        assertEquals(UPDATED_AT_1, clientDtoResponse.getBody().getUpdatedAt());
    }

    @Test
    public void delete() throws ClientNotFoundException {
        doNothing().when(clientService).delete(CLIENT_PUBLIC_ID_1);
        ResponseEntity<String> responseEntity = clientController.delete(CLIENT_PUBLIC_ID_1);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test(expected = ClientNotFoundHttpException.class)
    public void deleteClientNotFoundException() throws ClientNotFoundException {
        ClientNotFoundException clientNotFoundException = new ClientNotFoundException(DI_EXCEPTION_MESSAGE);
        doThrow(clientNotFoundException).when(clientService).delete(any(UUID.class));
        clientController.delete(CLIENT_PUBLIC_ID_1);
    }

    private List<ClientDto> buildClientDtoList(){

        List<ClientDto> clientDtoList = new ArrayList<>();

        clientDtoList.add(buildClient_1(CLIENT_ID_1, CLIENT_PUBLIC_ID_1, CREATED_AT_1, UPDATED_AT_1));

        clientDtoList.add(buildClient_1(CLIENT_ID_2, CLIENT_PUBLIC_ID_2, CREATED_AT_2, UPDATED_AT_2));

        clientDtoList.add(buildClient_1(CLIENT_ID_3, CLIENT_PUBLIC_ID_3, CREATED_AT_3, UPDATED_AT_3));

        return clientDtoList;
    }

    private ClientDto buildClient_1(Long clientId1, UUID clientPublicId1,
                                    LocalDateTime createdAt1, LocalDateTime updatedAt1) {
        return ClientDto.builder()
            .id(clientId1)
            .publicId(clientPublicId1)
            .createdAt(createdAt1)
            .updatedAt(updatedAt1)
            .build();
    }
}
