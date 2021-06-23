package com.arjay07.mycolorservice.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RegistrationDTO {

    @NotNull(message = "Data is invalid.")
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Data is invalid.")
    @NotBlank(message = "Username is required.")
    private String username;

    @NotNull(message = "Data is invalid.")
    @NotBlank(message = "Password is required")
    @Length(min = 8, message = "Password must be at least 8 characters.")
    @Length(max = 64, message = "Password must be no more than 64 characters.")
    private String password;

    @NotNull(message = "Data is invalid.")
    @Email
    private String email;
}
