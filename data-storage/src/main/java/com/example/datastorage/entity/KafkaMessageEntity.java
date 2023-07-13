package com.example.datastorage.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "kafka_message")
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageEntity extends BaseEntity {

    private String topic;
    private Long offsetKafka;
    private Integer partition;
    private String partitionKey;

}
