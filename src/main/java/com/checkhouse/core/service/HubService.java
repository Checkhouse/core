package com.checkhouse.core.service;

import com.checkhouse.core.repository.mysql.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;

}
