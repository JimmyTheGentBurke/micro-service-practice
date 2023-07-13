package org.example;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.common.kafka")
public class KafkaProperties {
    private List<String> bootstrapServers = new ArrayList<>();
    private String producerName;
    private String consumerGroupId;
    private String topic;

}
