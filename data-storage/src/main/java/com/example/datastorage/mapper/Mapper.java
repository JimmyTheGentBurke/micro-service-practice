package com.example.datastorage.mapper;

import org.springframework.stereotype.Component;

@Component
public interface Mapper<F, T> {
    T mapFrom(F object);

}
