package com.example.integration.configuration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "coinmarketcap", url = "https://pro-api.coinmarketcap.com", configuration = ClientConfiguration.class)
public interface CoinMarketCapClient {
    @RequestMapping(method = RequestMethod.GET, value = "/v1/cryptocurrency/listings/latest?limit=10")
    String getAllCurrencies(@RequestHeader("X-CMC_PRO_API_KEY") String API_KEY, @RequestHeader("Accept") String ACCEPT_HEADER);

}
