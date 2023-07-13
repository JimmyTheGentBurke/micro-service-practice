package org.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.KafkaMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ConsumerDeserializer implements Deserializer<KafkaMessage> {
    private final ObjectMapper objectMapper;

    @Override
    public KafkaMessage deserialize(String s, byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, KafkaMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
