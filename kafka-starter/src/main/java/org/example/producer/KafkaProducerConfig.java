package org.example.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.KafkaMessage;
import org.example.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProducerConfig {
    private final KafkaProperties properties;
    private final int PARTITIONS = 3;
    private final short REPLICATIONS = 3;

    @Bean(destroyMethod = "close")
    public KafkaProducer<String, KafkaMessage> createProducer(ProducerSerializer serializer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, properties.getProducerName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "prod-1");

        return new KafkaProducer<>(props, null, serializer);
    }

        @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public NewTopic newTopic() {
        return new NewTopic
                (properties.getTopic(), PARTITIONS, REPLICATIONS);
    }
}
