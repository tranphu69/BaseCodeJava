package com.example.baseCode.controller;

import com.example.baseCode.dto.request.ApiResponse;
import com.example.baseCode.dto.request.AuthenticationRequest;
import com.example.baseCode.dto.response.AuthenticationResponse;
import com.example.baseCode.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        boolean result = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        AuthenticationResponse authen = new AuthenticationResponse();
        authen.setAuthenticated(result);
        apiResponse.setResult(authen);
        return apiResponse;
    }
}
