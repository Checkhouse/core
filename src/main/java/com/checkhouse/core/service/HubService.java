package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.HubDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.dto.request.HubRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;
    private final AddressRepository addressRepository;

    HubDTO addHub(HubRequest.AddHubRequest req) {
        hubRepository.findHubByName(req.name()).ifPresent(hub -> {
            throw new GeneralException(ErrorStatus._HUB_ALREADY_EXISTS);
        });
        hubRepository.findHubByClusteredId(req.clusteredId()).ifPresent(hub -> {
            throw new GeneralException(ErrorStatus._HUB_CLUSTERED_ID_ALREADY_EXISTS);
        });
        Address addr = addressRepository.findById(req.address().getAddressId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND)
        );

        Hub savedHub = hubRepository.save(
                Hub.builder()
                        .hubId(req.hubId())
                        .name(req.name())
                        .address(addr)
                        .clusteredId(req.clusteredId())
                        .build()
        );
        return savedHub.toDto();
    }
    HubDTO updateHub(HubRequest.UpdateHubRequest req) {
        Hub hub = hubRepository.findById(req.hubId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND)
        );
        hubRepository.findHubByName(req.name()).ifPresent(h -> {
            throw new GeneralException(ErrorStatus._HUB_ALREADY_EXISTS);
        });
        hubRepository.findHubByClusteredId(req.clusteredId()).ifPresent(h -> {
            throw new GeneralException(ErrorStatus._HUB_CLUSTERED_ID_ALREADY_EXISTS);
        });
        Address addr = addressRepository.findById(req.address().getAddressId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND)
        );
        hub.UpdateAddress(addr);
        hub.UpdateName(req.name());
        hub.UpdateClusteredId(req.clusteredId());
        return hub.toDto();
    }
    void deleteHub(HubRequest.DeleteHubRequest req) {
        Hub hub = hubRepository.findById(req.hubId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND)
        );
        hubRepository.delete(hub);
    }
    List<HubDTO> getHubs() {
        return hubRepository.findAll()
                .stream()
                .map(Hub::toDto)
                .toList();
    }


    //TODO: ALLOCate 구현
    HubDTO allocateHub(HubRequest.AllocateHubRequest req) {return null;}

}
