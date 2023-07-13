//package org.example.consumer;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.common.errors.WakeupException;
//import org.example.KafkaMessage;
//import org.example.KafkaProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.event.ContextClosedEvent;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaConsumerService {
//    private static final Logger log = LoggerFactory.getLogger(ActionProcessor.class);
//    public static final int MAX_MESSAGE_PROCESS_RETRY = 600;
//    private final Consumer<String, KafkaMessage<?>> consumer;
//    private final ActionProcessor processor;
//    private final KafkaProperties properties;
//
//    private final Thread consumerThread = new Thread(
//            this::runConsumer,
//            this.getClass().getSimpleName()
//    );
//
//    @EventListener
//    public void startEventCycle(ContextRefreshedEvent event) {
//        consumerThread.start();
//    }
//
//    private volatile boolean exitFlag = true;
//
//    @EventListener(ContextClosedEvent.class)
//    public void stopEventCycle() {
//        exitFlag = false;
//        consumer.wakeup();
//    }
//
//    void runConsumer() {
//        try {
//            consumer.subscribe(List.of(properties.getTopic()));
//            while (exitFlag) {
//                final ConsumerRecords<String, KafkaMessage<?>> consumerRecords = consumer.poll(Duration.ofMillis(5000));
//                boolean messageProcessingNotFinished;
//                int failCount = 0;
//                do {
//                    try {
//                        processMessages(consumerRecords);
//                        messageProcessingNotFinished = false;
//                    } catch (Exception ex) {
//                        messageProcessingNotFinished = true;
//                        failCount++;
//                        if (failCount > MAX_MESSAGE_PROCESS_RETRY) {
//                            System.exit(13);
//                        } else {
//                            Thread.sleep(10000);
//                        }
//                    }
//                } while (messageProcessingNotFinished);
//                consumer.commitAsync();
//            }
//        } catch (InterruptedException ex) {
//            exitFlag = false;
//            log.error("{} tread execution interrupted", getClass().getSimpleName(), ex);
//        } catch (WakeupException ex) {
//            log.info("{}tread finish execution", getClass().getSimpleName(), ex);
//        } catch (Exception ex) {
//            log.info("{}kafka internal error");
//        } finally {
//            consumer.unsubscribe();
//        }
//    }
//
//    private void processMessages(ConsumerRecords<String, KafkaMessage<?>> consumerRecords) {
//        for (ConsumerRecord<String, KafkaMessage<?>> record : consumerRecords) {
//            KafkaMessage<?> value = record.value();
//            value.setPartitionKey(record.key());
//            if (value != null) {
//                processor.processAction(value);
//            }
//        }
//    }
//}
