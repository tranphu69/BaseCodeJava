package com.example.baseCode.controller;

import com.example.baseCode.dto.request.IntrospectRequest;
import com.example.baseCode.dto.request.LogoutRequest;
import com.example.baseCode.dto.request.RefreshRequest;
import com.example.baseCode.dto.response.ApiResponse;
import com.example.baseCode.dto.request.AuthenticationRequest;
import com.example.baseCode.dto.response.AuthenticationResponse;
import com.example.baseCode.dto.response.IntrospecResponse;
import com.example.baseCode.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse result = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        AuthenticationResponse authen = new AuthenticationResponse();
        authen.setAuthenticated(result.isAuthenticated());
        authen.setToken(result.getToken());
        apiResponse.setResult(authen);
        return apiResponse;
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospecResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        ApiResponse<IntrospecResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        return apiResponse;
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        authenticationService.logout(logoutRequest);
        return apiResponse;
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException{
        var result = authenticationService.refreshToken(request);
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        return apiResponse;
    }
}
