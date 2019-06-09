package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.ClientEntity;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.repository.ClientRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(JUnit4.class)
public class ClientServiceTest {

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    @Spy
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {

        List<ClientEntity> clientEntities = buildClientList();
        Page<ClientEntity> clientEntitiesPage = new PageImpl<>(clientEntities);

        when(clientRepository.findAll(any(Pageable.class)))
            .thenReturn(clientEntitiesPage);

        Page<ClientDto> clientDtos = clientService.findAll(PageRequest.of(1, 1));

        assertNotNull(clientDtos);
        assertEquals(3,clientDtos.getContent().size());
        assertEquals(CLIENT_PUBLIC_ID_1,clientDtos.getContent().get(0).getPublicId());
        assertEquals(CREATED_AT_1,clientDtos.getContent().get(0).getCreatedAt());
        assertEquals(UPDATED_AT_1,clientDtos.getContent().get(0).getUpdatedAt());
        assertEquals(CLIENT_PUBLIC_ID_2,clientDtos.getContent().get(1).getPublicId());
        assertEquals(CREATED_AT_2,clientDtos.getContent().get(1).getCreatedAt());
        assertEquals(UPDATED_AT_2,clientDtos.getContent().get(1).getUpdatedAt());
        assertEquals(CLIENT_PUBLIC_ID_3,clientDtos.getContent().get(2).getPublicId());
        assertEquals(CREATED_AT_3,clientDtos.getContent().get(2).getCreatedAt());
        assertEquals(UPDATED_AT_3,clientDtos.getContent().get(2).getUpdatedAt());
    }

    @Test
    public void findByPublicId() throws ClientNotFoundException {

        when(clientRepository.findByPublicId(CLIENT_PUBLIC_ID_1))
            .thenReturn(Optional.of(ClientEntity.builder()
                .id(CLIENT_ID_1)
                .publicId(CLIENT_PUBLIC_ID_1)
                .createdAt(CREATED_AT_1)
                .updatedAt(UPDATED_AT_1)
                .build()));

        ClientDto clientDto = clientService.findByPublicId(CLIENT_PUBLIC_ID_1);
        assertNotNull(clientDto);
        assertEquals(CLIENT_PUBLIC_ID_1,clientDto.getPublicId());
        assertEquals(CREATED_AT_1,clientDto.getCreatedAt());
        assertEquals(UPDATED_AT_1,clientDto.getUpdatedAt());
    }

    @Test(expected = ClientNotFoundException.class)
    public void findByPublicIdClientNotFonudException() throws ClientNotFoundException {

        when(clientRepository.findByPublicId(CLIENT_PUBLIC_ID_1))
            .thenReturn(Optional.empty());

        clientService.findByPublicId(CLIENT_PUBLIC_ID_1);
    }

    @Test
    public void create() {

        when(clientRepository.save(any(ClientEntity.class)))
            .thenReturn(ClientEntity.builder()
                .id(CLIENT_ID_1)
                .publicId(CLIENT_PUBLIC_ID_1)
                .createdAt(CREATED_AT_1)
                .updatedAt(UPDATED_AT_1)
                .build());
        ClientDto clientDto = clientService.create();
        assertNotNull(clientDto);
        assertEquals(CLIENT_PUBLIC_ID_1,clientDto.getPublicId());
        assertEquals(CREATED_AT_1,clientDto.getCreatedAt());
        assertEquals(UPDATED_AT_1,clientDto.getUpdatedAt());
    }

    @Test
    public void delete() throws ClientNotFoundException {
        int invocations = 1;
        ClientEntity clientEntity = ClientEntity.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLIC_ID_1)
            .createdAt(CREATED_AT_1)
            .updatedAt(UPDATED_AT_1)
            .build();
        when(clientRepository.findByPublicId(CLIENT_PUBLIC_ID_1))
            .thenReturn(Optional.of(clientEntity));
        doNothing().when(clientRepository).delete(any(ClientEntity.class));
        clientService.delete(CLIENT_PUBLIC_ID_1);
        verify(clientRepository, times(invocations)).delete(clientEntity);

    }

    @Test(expected = ClientNotFoundException.class)
    public void deleteClientNotFoundException() throws ClientNotFoundException {

        when(clientRepository.findByPublicId(CLIENT_PUBLIC_ID_1))
            .thenReturn(Optional.empty());
        doNothing().when(clientRepository).delete(any(ClientEntity.class));
        clientService.delete(CLIENT_PUBLIC_ID_1);
    }

    private List<ClientEntity> buildClientList(){

        List<ClientEntity> clientList = new ArrayList<>();

        clientList.add(ClientEntity.builder()
            .id(CLIENT_ID_1)
            .publicId(CLIENT_PUBLIC_ID_1)
            .createdAt(CREATED_AT_1)
            .updatedAt(UPDATED_AT_1)
            .build());

        clientList.add(ClientEntity.builder()
            .id(CLIENT_ID_2)
            .publicId(CLIENT_PUBLIC_ID_2)
            .createdAt(CREATED_AT_2)
            .updatedAt(UPDATED_AT_2)
            .build());

        clientList.add(ClientEntity.builder()
            .id(CLIENT_ID_3)
            .publicId(CLIENT_PUBLIC_ID_3)
            .createdAt(CREATED_AT_3)
            .updatedAt(UPDATED_AT_3)
            .build());

        return clientList;
    }
}
