package com.checkhouse.core.service;

import com.checkhouse.core.dto.request.HubRequest;
import com.checkhouse.core.entity.es.HubDocument;
import com.checkhouse.core.entity.es.RegionDocument;
import com.checkhouse.core.entity.es.ZoneDocument;
import com.checkhouse.core.repository.es.HubDocumentRepository;
import com.checkhouse.core.repository.es.RegionRepository;
import com.checkhouse.core.repository.es.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class HubEsService {
    private final ElasticsearchOperations client;
    private final HubDocumentRepository hubDocumentRepository;
    private final RegionRepository regionRepository;
    private final ZoneRepository zoneRepository;

    public ZoneDocument searchZone(Double longitude, Double latitude) {
        // 경위도 입력
        // 사용자의 경위도를 기반으로 region 검색
        // 사용자의 존은 무조건 1개 이므로 첫번째 요소만 선택
        List<RegionDocument> regionDocument = regionRepository.findRegionDocumentByPoint(longitude, latitude);
        log.info("{}",regionDocument.getFirst().getRegionId());
        // 해당 region을 가지고 있는 zone 검색
        return zoneRepository.findByAreasIdsContaining(regionDocument.getFirst().getRegionId()).getFirst();
    }
    public HubDocument addHub(HubRequest.SaveHubEsRequest request, Integer zoneId) {
        // mysql 저장 및 es 저장

        return hubDocumentRepository.save(
                HubDocument.builder()
                        .hubId(request.hubId().toString()) // 실제로는 mysql에 저장된 id 사용
                        .name(request.name())
                        .address(request.address())
                        .location(GeoJsonPoint.of(request.longitude(), request.latitude()))
                        .zoneId(zoneId)
                        .build()
        );
    }
    public HubDocument searchHub(Integer zoneId) {
        return hubDocumentRepository.findByZoneId(zoneId).getFirst();
    }
}
