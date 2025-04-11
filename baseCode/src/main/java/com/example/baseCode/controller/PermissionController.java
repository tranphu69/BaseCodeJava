package com.example.baseCode.controller;

import com.example.baseCode.dto.request.PermissionRequest;
import com.example.baseCode.dto.response.ApiResponse;
import com.example.baseCode.dto.response.PermissionResponse;
import com.example.baseCode.entity.Permission;
import com.example.baseCode.service.PermissionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        Permission permission = permissionService.create(request);
        PermissionResponse response = modelMapper.map(permission, PermissionResponse.class);
        apiResponse.setResult(response);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getList(){
        List<Permission> permissions = permissionService.getList();
        List<PermissionResponse> responses = permissions.stream()
                .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                .collect(Collectors.toList());
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(responses);
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePermission(@PathVariable("id") String id){
        permissionService.delete(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult("Delete permission successfull!!!");
        return apiResponse;
    }
}
