package com.example.datastorage;

import com.example.cache.EnableCacheConfiguration;
import org.example.EnableKafkaConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableCacheConfiguration
@EnableKafkaConsumerConfiguration
public class DataStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataStorageApplication.class, args);
    }

}
