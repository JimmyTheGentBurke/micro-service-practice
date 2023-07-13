package com.example.datastorage.service;


import com.example.datastorage.dto.request.CryptocurrencyRequest;
import com.example.datastorage.entity.CryptocurrencyEntity;
import com.example.datastorage.repository.CryptocurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Cacheable(cacheNames = {"cryptocurrency"}, key = "#request.symbol")
    public CryptocurrencyEntity getCurrencyBySymbol(CryptocurrencyRequest request) {
        CryptocurrencyEntity entity = cryptocurrencyRepository.findBySymbol(request.getSymbol())
                .orElseThrow();
        return entity;
    }

    public List<CryptocurrencyEntity> getAllCurrencies() {
        return cryptocurrencyRepository.findAll();
    }

}
