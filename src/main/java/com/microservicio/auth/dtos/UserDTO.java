package com.microservicio.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private Long id;
  private String role;
  private String username;
  private String name;
  private String surname;

}
