package com.wirecardchallenge.rest.controller.client;

import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.service.ClientService;
import com.wirecardchallenge.core.service.ExceptionMessages;
import com.wirecardchallenge.rest.exception.client.ClientNotFoundHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/client")
@Slf4j
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientDto>> findAll(Pageable pageRequest){
        Page<ClientDto> clientDtos = clientService.findAll(pageRequest);
        return ResponseEntity.ok(clientDtos);
    }

    @GetMapping(value = "/{publicId}")
    public ResponseEntity<ClientDto> findByPublicId(@PathVariable UUID publicId) {
        ClientDto clientDtos;
        try {
            clientDtos = clientService.findByPublicId(publicId);
        } catch (ClientNotFoundException e) {
            throw new ClientNotFoundHttpException(ExceptionMessages.CLIENT_NOT_FOUND + "  " + publicId);
        }
        return ResponseEntity.ok(clientDtos);
    }

    @PostMapping
    public ResponseEntity<ClientDto> add(){
        ClientDto clientDtoSaved = clientService.create();
        return ResponseEntity.ok(clientDtoSaved);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> delete(@PathVariable UUID publicId) {
        try {
            clientService.delete(publicId);
            return new ResponseEntity<>("{'message': 'Bye bye " + publicId + "'}", HttpStatus.OK);
        } catch (ClientNotFoundException e) {
            throw new ClientNotFoundHttpException(ExceptionMessages.CLIENT_NOT_FOUND + "  " + publicId);
        }
    }
}
