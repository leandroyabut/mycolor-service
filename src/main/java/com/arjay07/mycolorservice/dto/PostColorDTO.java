package com.arjay07.mycolorservice.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Builder
public class PostColorDTO implements Serializable {
    @NotBlank(message = "Color name is required.")
    private String name;
    @Pattern(regexp = "(^([a-z0-9]{3})$)|(^([a-z0-9]{6})$)", message = "Color hex value is not in the correct format.")
    private String hex;
}
