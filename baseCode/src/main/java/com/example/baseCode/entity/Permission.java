package com.example.baseCode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Permission {
    @Id
    private String name;
    private String description;
}
