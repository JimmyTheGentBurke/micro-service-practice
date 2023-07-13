package com.example.datastorage.repository;

import com.example.datastorage.entity.CryptocurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<CryptocurrencyEntity, Long> {
    Optional<CryptocurrencyEntity> findBySymbol(String symbol);

}
