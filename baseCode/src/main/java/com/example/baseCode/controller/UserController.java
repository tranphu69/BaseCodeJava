package com.example.baseCode.controller;

import com.example.baseCode.dto.response.ApiResponse;
import com.example.baseCode.dto.request.UserCreateRequest;
import com.example.baseCode.dto.request.UserUpdateRequest;
import com.example.baseCode.dto.response.PermissionResponse;
import com.example.baseCode.dto.response.RoleResponse;
import com.example.baseCode.dto.response.UserResponse;
import com.example.baseCode.entity.User;
import com.example.baseCode.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.createUser(request);
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles().stream()
                .map(role -> {
                    RoleResponse roleResponses = modelMapper.map(role, RoleResponse.class);
                    Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                            .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                            .collect(Collectors.toSet());
                    roleResponses.setPermission(new HashSet<>(permissionResponses));
                    return roleResponses;
                })
                .collect(Collectors.toSet()));
        apiResponse.setResult(response);
        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<UserResponse>> listUser() {
        List<User> users = userService.listUser();
        List<UserResponse> userResponses = users.stream()
                .map(user -> {
                    UserResponse response = modelMapper.map(user, UserResponse.class);
                    response.setRoles(user.getRoles().stream()
                            .map(role -> {
                                RoleResponse roleResponses = modelMapper.map(role, RoleResponse.class);
                                Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                                        .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                                        .collect(Collectors.toSet());
                                roleResponses.setPermission(new HashSet<>(permissionResponses));
                                return roleResponses;
                            })
                            .collect(Collectors.toSet()));
                    return response;
                })
                .collect(Collectors.toList());
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponses);
        return apiResponse;
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> idUser (@PathVariable("id") String id){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.idUser(id);
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles().stream()
                .map(role -> {
                    RoleResponse roleResponses = modelMapper.map(role, RoleResponse.class);
                    Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                            .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                            .collect(Collectors.toSet());
                    roleResponses.setPermission(new HashSet<>(permissionResponses));
                    return roleResponses;
                })
                .collect(Collectors.toSet()));
        apiResponse.setResult(response);
        return apiResponse;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo(){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.getMyInfo();
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles().stream()
                .map(role -> {
                    RoleResponse roleResponses = modelMapper.map(role, RoleResponse.class);
                    Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                            .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                            .collect(Collectors.toSet());
                    roleResponses.setPermission(new HashSet<>(permissionResponses));
                    return roleResponses;
                })
                .collect(Collectors.toSet()));
        apiResponse.setResult(response);
        return apiResponse;
    }

    @PutMapping
    public ApiResponse<UserResponse> updateUser (@RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.updateUser(request);
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles().stream()
                .map(role -> {
                    RoleResponse roleResponses = modelMapper.map(role, RoleResponse.class);
                    Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                            .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                            .collect(Collectors.toSet());
                    roleResponses.setPermission(new HashSet<>(permissionResponses));
                    return roleResponses;
                })
                .collect(Collectors.toSet()));
        apiResponse.setResult(response);
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult("Delete user successfull!!!");
        return apiResponse;
    }

}
