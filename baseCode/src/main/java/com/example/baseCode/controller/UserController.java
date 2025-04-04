package com.example.baseCode.controller;

import com.example.baseCode.dto.request.ApiResponse;
import com.example.baseCode.dto.request.UserCreateRequest;
import com.example.baseCode.dto.request.UserUpdateRequest;
import com.example.baseCode.entity.User;
import com.example.baseCode.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    public List<User> listUser(){
        return userService.listUser();
    }

    @GetMapping("/{id}")
    public User idUser (@PathVariable("id") String id){
        return userService.idUser(id);
    }

    @PutMapping
    public User updateUser (@RequestBody UserUpdateRequest request){
        return userService.updateUser(request);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
        return "Delete user successfull!!!";
    }

}
