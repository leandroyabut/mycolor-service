package com.arjay07.mycolorservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationDTO {

    @NotNull(message = "Data is invalid.")
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Data is invalid.")
    @NotBlank(message = "Username is required.")
    private String username;

    @NotNull(message = "Data is invalid.")
    @NotBlank(message = "Password is required")
    @Min(value = 8, message = "Password must be at least 8 characters.")
    @Max(value = 16, message = "Password much be no more than 16 characters.")
    private String password;

    @NotNull(message = "Data is invalid.")
    @Email
    private String email;
}
