package com.example.baseCode.service;

import com.example.baseCode.dto.request.AuthenticationRequest;
import com.example.baseCode.exception.AppException;
import com.example.baseCode.exception.ErrorCode;
import com.example.baseCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
