package com.microservicio.auth.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SignUpDTO {

  @NotBlank(message = "Username may not be empty")
  private String username;

  @NotBlank(message = "Enabled may not be empty")
  private String name;

  @NotBlank(message = "Enabled may not be empty")
  private Boolean enabled;

  @NotBlank(message = "Role may not be empty")
  private String role;

  @NotBlank(message = "Password may not be empty")
  private String password;

  @NotBlank(message = "Password Verification may not be empty")
  private String passwordVerification;

}
