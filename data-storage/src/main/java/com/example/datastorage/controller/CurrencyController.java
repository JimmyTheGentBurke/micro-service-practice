package com.example.datastorage.controller;


import com.example.datastorage.dto.request.CryptocurrencyRequest;
import com.example.datastorage.entity.CryptocurrencyEntity;
import com.example.datastorage.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/trending/cryptocurrency/{symbol}")
    public ResponseEntity<CryptocurrencyEntity> getCurrency(@PathVariable String symbol) {
        return ResponseEntity.ok(currencyService.getCurrencyBySymbol(CryptocurrencyRequest.builder().symbol(symbol).build()));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<CryptocurrencyEntity>> getAllCurrency() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

}
