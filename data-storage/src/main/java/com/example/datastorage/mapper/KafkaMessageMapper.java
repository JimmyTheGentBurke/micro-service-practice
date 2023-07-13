package com.example.datastorage.mapper;

import com.example.datastorage.entity.KafkaMessageEntity;
import org.example.KafkaMessage;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageMapper implements Mapper<KafkaMessage,KafkaMessageEntity>{
    @Override
    public KafkaMessageEntity mapFrom(KafkaMessage object) {
        return new KafkaMessageEntity()
                .setTopic(object.getTopic())
                .setOffsetKafka(object.getOffset())
                .setPartition(object.getPartition())
                .setPartitionKey(object.getPartitionKey());
    }
}
