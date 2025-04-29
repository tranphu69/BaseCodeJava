package com.example.baseCode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class InvalidatedToken {
    @Id
    private String id;
    private Date expiryTime;
}
