package com.example.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.common.cache")
public class CacheConfigurationProperties {
    private String host;
    private int port;

}

