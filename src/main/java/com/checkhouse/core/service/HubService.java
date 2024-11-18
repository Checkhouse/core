package com.checkhouse.core.service;

import com.checkhouse.core.dto.HubDTO;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.request.HubRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;

    HubDTO AddHub(HubRequest.AddHubRequest req) {return null;}
    HubDTO UpdateHub(HubRequest.UpdateHubRequest req) {return null;}
    void DeleteHub(HubRequest.DeleteHubRequest req) {}
    List<HubDTO> GetHubs() {return null;}
    //TODO: ALLOCate 구현
    HubDTO AllocateHub(HubRequest.AllocateHubRequest req) {return null;}

}
