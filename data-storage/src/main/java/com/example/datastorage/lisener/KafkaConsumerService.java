package com.example.datastorage.lisener;

import com.example.datastorage.mapper.KafkaMessageMapper;
import com.example.datastorage.repository.KafkaMessageRepository;
import com.example.datastorage.service.CryptocurrencyService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.example.KafkaMessage;
import org.example.KafkaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final KafkaMessageRepository kafkaMessageRepository;
    private final PlatformTransactionManager transactionManager;
    private final CryptocurrencyService cryptocurrencyService;
    public static final int MAX_MESSAGE_PROCESS_RETRY = 600;
    private final Consumer<String, KafkaMessage> consumer;
    private final KafkaMessageMapper kafkaMessageMapper;
    private final KafkaProperties properties;


    private final Thread consumerThread = new Thread(
            this::runConsumer,
            this.getClass().getSimpleName()
    );

    @EventListener
    public void startEventCycle(ContextRefreshedEvent event) {
        consumerThread.start();
    }

    private volatile boolean exitFlag = true;

    @EventListener(ContextClosedEvent.class)
    public void stopEventCycle() {
        exitFlag = false;
        consumer.wakeup();
    }

    private class Listener implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            for (TopicPartition partition : partitions) {
                if (kafkaMessageRepository.findFirstByPartitionOrderByOffsetKafkaDesc(partition.partition()).isPresent()) {
                    consumer.seek(partition, kafkaMessageRepository.findFirstByPartitionOrderByOffsetKafkaDesc(partition.partition()).get().getOffsetKafka());

                } else {
                    consumer.seek(partition, 0);
                }
            }
        }
    }

    void runConsumer() {
        try {
            consumer.subscribe(List.of(properties.getTopic()), new Listener());
            while (exitFlag) {
                final ConsumerRecords<String, KafkaMessage> consumerRecords = consumer.poll(Duration.ofMillis(5000));
                boolean messageProcessingNotFinished;
                int failCount = 0;
                do {
                    try {
                        processMessages(consumerRecords);
                        messageProcessingNotFinished = false;
                    } catch (Exception ex) {
                        messageProcessingNotFinished = true;
                        failCount++;
                        if (failCount > MAX_MESSAGE_PROCESS_RETRY) {
                            System.exit(13);
                        } else {
                            Thread.sleep(10000);
                        }
                    }
                } while (messageProcessingNotFinished);
            }
        } catch (InterruptedException ex) {
            exitFlag = false;
            log.error("{} tread execution interrupted", getClass().getSimpleName(), ex);
        } catch (WakeupException ex) {
            log.info("{}tread finish execution", getClass().getSimpleName(), ex);
        } catch (Exception ex) {
            log.info("{}kafka internal error");
        } finally {
            consumer.unsubscribe();
        }
    }

    private void processMessages(ConsumerRecords<String, KafkaMessage> consumerRecords) {
        for (ConsumerRecord<String, KafkaMessage> record : consumerRecords) {
            KafkaMessage message = record.value();
            message.setTopic(record.topic());
            message.setPartition(record.partition());
            message.setOffset(record.offset());

            TransactionDefinition txDefinition = new DefaultTransactionDefinition();
            TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
            try {
                kafkaMessageRepository.save(kafkaMessageMapper.mapFrom(message));
//                cryptocurrencyService.processCryptocurrencies(message.getValue());
                transactionManager.commit(txStatus);
            } catch (Exception ex) {
                log.error("{Transaction error, check logic of parsing}", ex);
                transactionManager.rollback(txStatus);
            }
        }
    }
}
