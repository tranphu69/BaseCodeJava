package com.example.baseCode.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private boolean authenticated;
    private String token;
}
