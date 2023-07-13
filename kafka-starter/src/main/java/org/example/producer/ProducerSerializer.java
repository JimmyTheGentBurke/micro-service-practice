package org.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.example.KafkaMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProducerSerializer implements Serializer<KafkaMessage> {
    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String s, KafkaMessage kafkaMessage) {
        try {
            return objectMapper.writeValueAsBytes(kafkaMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
