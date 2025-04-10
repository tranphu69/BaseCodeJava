package com.example.baseCode.service;

import com.example.baseCode.dto.request.UserCreateRequest;
import com.example.baseCode.dto.request.UserUpdateRequest;
import com.example.baseCode.entity.User;
import com.example.baseCode.enums.Role;
import com.example.baseCode.exception.AppException;
import com.example.baseCode.exception.ErrorCode;
import com.example.baseCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserCreateRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXSITED);
        User user = new User();
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listUser(){
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public User idUser(String id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(UserUpdateRequest request){
        User user = userRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public User getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND)
        );
        return user;
    }
}
