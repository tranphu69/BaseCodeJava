package com.example.baseCode.dto.request;

import com.example.baseCode.entity.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleRequest {
    private String name;
    private String description;
    private Set<String> permission;
}
