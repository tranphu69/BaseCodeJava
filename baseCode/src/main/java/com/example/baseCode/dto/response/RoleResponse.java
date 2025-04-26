package com.example.baseCode.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse {
    private String name;
    private String description;
    private Set<PermissionResponse> permission;
}
