package org.example.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.KafkaMessage;
import org.example.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaSendService {
    private final KafkaProperties kafkaProperties;
    private final Producer<String, KafkaMessage> producer;

    public void sendKafkaMessage(KafkaMessage kafkaMessage) {
        ProducerRecord<String, KafkaMessage> record =
                new ProducerRecord<>(
                        kafkaProperties.getTopic(),
                        kafkaMessage.getPartitionKey(),
                        kafkaMessage
                );
        try {
            producer.send(record).get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof RuntimeException) {
                throw (RuntimeException) ex.getCause();
            } else {
                throw new RuntimeException("Unable send message in Kafka");
            }
        }
    }
}
