package com.example.baseCode.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Role {
    @Id
    private String name;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "permission_name")
    )
    private Set<Permission> permissions;
}
