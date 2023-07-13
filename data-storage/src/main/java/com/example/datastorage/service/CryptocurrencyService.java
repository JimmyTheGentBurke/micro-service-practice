package com.example.datastorage.service;

import com.example.datastorage.dto.Cryptocurrency;
import com.example.datastorage.entity.CryptocurrencyEntity;
import com.example.datastorage.mapper.CurrencyResponseMapper;
import com.example.datastorage.repository.CryptocurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CryptocurrencyService {
    private final ObjectMapper objectMapper;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CurrencyResponseMapper currencyResponseMapper;

    @SneakyThrows
    public void processCryptocurrencies(String value) {
        JsonNode data = objectMapper.readTree(value).get("data");
        List<CryptocurrencyEntity> entities = Arrays.stream(objectMapper.treeToValue(data, Cryptocurrency[].class))
                .map(currencyResponseMapper::mapFrom)
                .collect(Collectors.toList());

        if (cryptocurrencyRepository.count() == 0) {
            initDataBase(entities);
        } else {
            update(entities);
        }
    }

    @CachePut(cacheNames = {"cryptocurrency"}, key = "#entity.symbol")
    public CryptocurrencyEntity saveAndRefreshCache(CryptocurrencyEntity entity) {
        return cryptocurrencyRepository.save(entity);
    }

    private void initDataBase(List<CryptocurrencyEntity> entities) {
        entities.forEach(cryptocurrencyEntity -> cryptocurrencyRepository.save(cryptocurrencyEntity));
    }

    private void update(List<CryptocurrencyEntity> entities) {
        for (CryptocurrencyEntity updated : entities) {
            CryptocurrencyEntity beforeUpdate = cryptocurrencyRepository.findBySymbol(updated.getSymbol())
                    .orElseThrow();
            CryptocurrencyEntity entity = currencyResponseMapper.currencyUpdateSetter(beforeUpdate, updated);
            saveAndRefreshCache(entity);
        }
    }

}
