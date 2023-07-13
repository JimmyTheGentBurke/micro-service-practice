package com.example.datastorage.repository;

import com.example.datastorage.entity.KafkaMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface KafkaMessageRepository extends JpaRepository<KafkaMessageEntity, Long> {
    Optional<KafkaMessageEntity> findFirstByPartitionOrderByOffsetKafkaDesc(Integer partition);
}
