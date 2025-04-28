package com.example.baseCode.dto.request;

import com.example.baseCode.validator.DobContraint;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserCreateRequest {
    private String username;
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;
    @DobContraint(min = 16, message = "INVALID_DOB")
    private LocalDate dob;
    private Set<String> roles;
}
