package com.example.baseCode.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Set<String> roles;
}