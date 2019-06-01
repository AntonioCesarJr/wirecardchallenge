package com.wirecardchallenge.rest.controller.client;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.service.ClientService;
import com.wirecardchallenge.rest.controller.client.request.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

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

    @GetMapping(value = "/{publicId}")
    public ResponseEntity<ClientDto> findByPublicId(@PathVariable UUID publicId){
        ClientDto clientDtos = clientService.findByPublicId(publicId);
        return ResponseEntity.ok(clientDtos);
    }

    @PostMapping
    public ResponseEntity<ClientDto> add(@RequestBody @Valid ClientRequest clientRequest){
        ClientDto clientDto = buildClientDto(clientRequest);
        ClientDto clientDtoSaved = clientService.create(clientDto);
        return ResponseEntity.ok(clientDtoSaved);
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<ClientDto> update(@PathVariable UUID publicId,
                                            @RequestBody @Valid ClientRequest clientRequest){
        ClientDto clientDto = buildClientDto(clientRequest);
        clientDto.setPublicId(publicId);
        ClientDto clientDtoSaved = clientService.update(publicId, clientDto);
        return ResponseEntity.ok(clientDtoSaved);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<ClientDto> delete(@PathVariable UUID publicId){
        clientService.delete(publicId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ClientDto buildClientDto(ClientRequest clientRequest){
        return ClientDto.builder()
                .name(clientRequest.getName())
                .description(clientRequest.getDescription())
                .build();
    }
}
