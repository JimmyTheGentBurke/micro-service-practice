package com.example.datastorage.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyJson implements Serializable {
    private String tags;

}