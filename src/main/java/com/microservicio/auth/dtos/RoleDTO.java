package com.microservicio.auth.dtos;

import com.microservicio.auth.entities.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

  private Integer id;
  private String name;

  public RoleDTO(final RoleEnum roleEnum) {
    this.id = roleEnum.getId();
    this.name = roleEnum.getName();
  }
}
