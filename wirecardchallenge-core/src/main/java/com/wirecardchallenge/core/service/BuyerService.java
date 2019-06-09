package com.wirecardchallenge.core.service;

import com.wirecardchallenge.core.dto.BuyerDto;
import com.wirecardchallenge.core.dto.ClientDto;
import com.wirecardchallenge.core.entity.BuyerEntity;
import com.wirecardchallenge.core.entity.ClientEntity;
import com.wirecardchallenge.core.exceptions.buyer.BuyerNotFoundException;
import com.wirecardchallenge.core.exceptions.buyer.BuyerServiceIntegrityConstraintException;
import com.wirecardchallenge.core.exceptions.client.ClientNotFoundException;
import com.wirecardchallenge.core.repository.BuyerRepository;
import com.wirecardchallenge.core.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BuyerService {

    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    ClientRepository clientRepository;

    public Page<BuyerDto> findAll(Pageable pageable){

        Page<BuyerEntity> buyerPage = buyerRepository.findAll(pageable);
        List<BuyerDto> buyerDtos = buyerPage.getContent().stream()
            .map(this::buildBuyerDto)
            .collect(Collectors.toList());

        return new PageImpl<>(buyerDtos, pageable, buyerPage.getTotalElements());
    }

    public BuyerDto findByPublicId(UUID publicId) throws BuyerNotFoundException {

        Optional<BuyerEntity> buyerOptional = buyerRepository.findByPublicId(publicId);

        if (!buyerOptional.isPresent()) throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);
        return buildBuyerDto(buyerOptional.get());
    }

    public BuyerDto create(BuyerDto buyerDto) throws ClientNotFoundException,
        BuyerServiceIntegrityConstraintException {

        Optional<ClientEntity> client = clientRepository.findByPublicId(buyerDto.getClientDto().getPublicId());
        if(!client.isPresent()) throw new ClientNotFoundException(ExceptionMessages.CLIENT_NOT_FOUND);

        BuyerEntity buyerEntity = buildBuyer(buyerDto);
        return saveBuyerOrThrow(client.get(), buyerEntity);
    }

    public BuyerDto update(UUID uuid,
                           BuyerDto buyerDto) throws ClientNotFoundException,

        BuyerNotFoundException, BuyerServiceIntegrityConstraintException {

        Optional<ClientEntity> client = clientRepository.findByPublicId(buyerDto.getClientDto().getPublicId());

        if(!client.isPresent()) throw new ClientNotFoundException(ExceptionMessages.CLIENT_NOT_FOUND);
        Optional<BuyerEntity> optionalBuyer = buyerRepository.findByPublicId(uuid);

        if (!optionalBuyer.isPresent()) throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);

        BuyerEntity buyerEntity = optionalBuyer.get();
        buyerEntity.setName(buyerDto.getName());
        buyerEntity.setEmail(buyerDto.getEmail());
        buyerEntity.setCpf(buyerDto.getCpf());
        return saveBuyerOrThrow(client.get(), buyerEntity);
    }

    public void delete(UUID uuid) throws BuyerNotFoundException, BuyerServiceIntegrityConstraintException {

        Optional<BuyerEntity> buyerOptional = buyerRepository.findByPublicId(uuid);

        if (!buyerOptional.isPresent()) throw new BuyerNotFoundException(ExceptionMessages.BUYER_NOT_FOUND);

        BuyerEntity buyerEntity = buyerOptional.get();
        try{
            buyerRepository.delete(buyerEntity);
        }catch (DataIntegrityViolationException e){
            log.error(e.getMessage() + " // " + e.getCause().getCause());
            throw new BuyerServiceIntegrityConstraintException(e.getMessage());
        }
    }

    private BuyerDto saveBuyerOrThrow(ClientEntity clientEntity, BuyerEntity buyerEntity)
        throws BuyerServiceIntegrityConstraintException {
        buyerEntity.setClient(clientEntity);
        BuyerEntity buyerEntitySaved;
        try {
            buyerEntitySaved = buyerRepository.save(buyerEntity);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage() + " - " + e.getCause().getCause());
            throw new BuyerServiceIntegrityConstraintException(e.getMessage());
        }

        return buildBuyerDto(buyerEntitySaved);
    }

    private BuyerDto buildBuyerDto(BuyerEntity buyerEntity){
        return BuyerDto.builder()
            .publicId(buyerEntity.getPublicId())
            .email(buyerEntity.getEmail())
            .name(buyerEntity.getName())
            .cpf(buyerEntity.getCpf())
            .clientDto(ClientDto.builder()
                .publicId(buyerEntity.getClient().getPublicId())
                .build())
            .createdAt(buyerEntity.getCreatedAt())
            .updatedAt(buyerEntity.getUpdatedAt())
            .build();
    }

    private BuyerEntity buildBuyer(BuyerDto buyerDto){
        return BuyerEntity.builder()
            .id(buyerDto.getId())
            .publicId(buyerDto.getPublicId())
            .email(buyerDto.getEmail())
            .name(buyerDto.getName())
            .cpf(buyerDto.getCpf())
            .build();
    }
}
