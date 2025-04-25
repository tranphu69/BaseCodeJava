package com.example.baseCode.controller;

import com.example.baseCode.dto.request.RoleRequest;
import com.example.baseCode.dto.response.ApiResponse;
import com.example.baseCode.dto.response.PermissionResponse;
import com.example.baseCode.dto.response.RoleResponse;
import com.example.baseCode.entity.Role;
import com.example.baseCode.repository.RoleRepository;
import com.example.baseCode.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        Role role = roleService.create(request);
        RoleResponse roleResponse = modelMapper.map(role, RoleResponse.class);
        roleResponse.setPermission(role.getPermissions().stream()
                .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                .collect(Collectors.toSet()));
        apiResponse.setResult(roleResponse);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getList() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        List<Role> roles = roleService.getList();
        roles.forEach(role -> {
            role.setPermissions(roleRepository.findPermissionsByRoleName(role.getName()));
        });
        List<RoleResponse> responses = roles.stream()
                .map(role -> {
                    RoleResponse roleResponse = modelMapper.map(role, RoleResponse.class);
                    Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                            .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                            .collect(Collectors.toSet());
                    roleResponse.setPermission(permissionResponses);
                    return roleResponse;
                })
                .collect(Collectors.toList());
        apiResponse.setResult(responses);
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRole(@PathVariable("id") String id){
        roleService.delete(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult("Delete role successfull!!!");
        return apiResponse;
    }
}
