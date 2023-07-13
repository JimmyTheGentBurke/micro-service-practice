package org.example;

import org.example.consumer.KafkaConsumerConfig;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EnableKafka
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({KafkaConsumerConfig.class})
public @interface EnableKafkaConsumerConfiguration {
}
