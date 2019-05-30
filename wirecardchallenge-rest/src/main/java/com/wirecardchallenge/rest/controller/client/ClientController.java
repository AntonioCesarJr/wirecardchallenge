package com.wirecardchallenge.rest.controller.client;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.service.ClientService;
import com.wirecardchallenge.rest.controller.client.request.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/v1/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientDto>> findAll(Pageable pageRequest){
        Page<ClientDto> clientDtos = clientService.findAll(pageRequest);
        return ResponseEntity.ok(clientDtos);
    }

    @PostMapping
    public ResponseEntity<ClientDto> save(@RequestBody @Valid ClientRequest clientRequest){
        ClientDto clientDto = buildClientDto(clientRequest);
        ClientDto clientDtoSaved = clientService.save(clientDto);
        return ResponseEntity.ok(clientDtoSaved);
    }

    private ClientDto buildClientDto(ClientRequest clientRequest){
        return ClientDto.builder()
                .name(clientRequest.getName())
                .desccription(clientRequest.getDescription())
                .build();
    }

}
