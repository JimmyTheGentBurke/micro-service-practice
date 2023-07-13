package com.example.integration.service;

import com.example.integration.configuration.CoinMarketCapClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.KafkaMessage;
import org.example.producer.KafkaSendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinMarketCapService {
    @Value("${coin-market-cap.api-key}")
    private String apiKey;
    @Value("${coin-market-cap.accept-header}")
    private String acceptHeader;
    private final CoinMarketCapClient coinMarketCapClient;
    private final KafkaSendService kafkaSendService;
    private static int count;

    @SneakyThrows
    @Scheduled(cron = "0 */01 * ? * *")
//    @EventListener(ApplicationReadyEvent.class)
    public void loadCryptoData() {
        KafkaMessage stringKafkaMessage = new KafkaMessage();
        stringKafkaMessage.setPartitionKey("TestKey");
//        coinMarketCapClient.getAllCurrencies(apiKey, acceptHeader);
        stringKafkaMessage.setValue("Test Message " + count++);
        kafkaSendService.sendKafkaMessage(stringKafkaMessage);
    }

}

