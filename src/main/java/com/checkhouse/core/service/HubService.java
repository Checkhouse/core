package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.dto.HubDTO;
import com.checkhouse.core.dto.StockDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.Stock;
import com.checkhouse.core.entity.es.HubDocument;
import com.checkhouse.core.entity.es.RegionDocument;
import com.checkhouse.core.entity.es.ZoneDocument;
import com.checkhouse.core.repository.es.HubDocumentRepository;
import com.checkhouse.core.repository.es.RegionRepository;
import com.checkhouse.core.repository.es.ZoneRepository;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.dto.request.HubRequest;
import com.checkhouse.core.repository.mysql.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;
    private final AddressRepository addressRepository;
    private final StockRepository stockRepository;
    public HubDTO addHub(HubRequest.AddHubRequest req, AddressDTO addressDTO, int zoneId) {
        Address address = addressRepository.findById(addressDTO.addressId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND)
        );
        // 허브 es 저장
        Hub savedHub = hubRepository.save(
                Hub.builder()
                        .name(req.name())
                        .address(address)
                        .clusteredId(zoneId)
                        .build()
        );

        return savedHub.toDto();
    }
    public HubDTO updateHub(HubRequest.UpdateHubRequest req) {
        Hub hub = hubRepository.findById(req.hubId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND)
        );
        hubRepository.findHubByName(req.name()).ifPresent(h -> {
            throw new GeneralException(ErrorStatus._HUB_ALREADY_EXISTS);
        });
        hubRepository.findHubByClusteredId(req.clusteredId()).ifPresent(h -> {
            throw new GeneralException(ErrorStatus._HUB_CLUSTERED_ID_ALREADY_EXISTS);
        });
        Address addr = addressRepository.findById(req.addressId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND)
        );
        hub.UpdateAddress(addr);
        hub.UpdateName(req.name());
        hub.UpdateClusteredId(req.clusteredId());
        return hub.toDto();
    }
    public void deleteHub(HubRequest.DeleteHubRequest req) {
        Hub hub = hubRepository.findById(req.hubId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND)
        );
        hubRepository.delete(hub);
    }
    public List<HubDTO> getHubs() {
        return hubRepository.findAll()
                .stream()
                .map(Hub::toDto)
                .toList();
    }


    //TODO: ALLOCate 구현
    public HubDTO allocateHub(HubRequest.AllocateHubRequest req) {return null;}

    //Stock
    public StockDTO addStock(HubRequest.AddStockRequest req) {
        Hub hub = hubRepository.findById(req.hubId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND));

        Stock stock = Stock.builder()
                .stockId(req.stockId())
                .usedProductId(req.usedProductId())
                .area(req.area())
                .hub(hub)
                .build();

        Stock savedStock = stockRepository.save(stock);
        return savedStock.toDto();
    }
    public StockDTO updateStockArea(HubRequest.UpdateStockAreaRequest req) {
        Stock stock = stockRepository.findById(req.stockId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._STOCK_ID_NOT_FOUND));

        stock.updateArea(req.area());
        return  stock.toDto();
    }
    public StockDTO getStockByUsedProductId(HubRequest.GetStockByUsedProductIdRequest req) {
        Stock stock = stockRepository.findStockByUsedProductId(req.usedProductId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._STOCK_ID_NOT_FOUND));

        return stock.toDto();
    }
    public List<StockDTO> getStocksByHubId(HubRequest.GetStocksByHubIdRequest req) {
        Hub hub = hubRepository.findById(req.hubId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND));

        List<Stock> stocks = stockRepository.findStocksByHubHubId(hub.getHubId());
        return stocks.stream().map(Stock::toDto).toList();
    }
    public List<StockDTO> getStocksByArea(HubRequest.GetStocksByAreaRequest req) {
        Hub hub = hubRepository.findById(req.hubId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._HUB_ID_NOT_FOUND));

        List<Stock> stocks = stockRepository.findStocksByHubHubIdAndArea(hub.getHubId(), req.area());
        return stocks.stream().map(Stock::toDto).toList();
    }
    public void deleteStock(HubRequest.DeleteStockRequest req) {
        Stock stock = stockRepository.findById(req.stockId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._STOCK_ID_NOT_FOUND));

        stockRepository.delete(stock);
    }

}
