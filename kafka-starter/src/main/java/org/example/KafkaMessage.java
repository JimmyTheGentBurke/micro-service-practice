package org.example;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class KafkaMessage {
    private String topic;
    private Long offset;
    private Integer partition;
    private String partitionKey;
    private String value;


}
