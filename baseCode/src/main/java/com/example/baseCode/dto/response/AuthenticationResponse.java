package com.example.baseCode.dto.response;

public class AuthenticationResponse {
    private boolean authenticated;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
