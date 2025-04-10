package com.example.baseCode.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
