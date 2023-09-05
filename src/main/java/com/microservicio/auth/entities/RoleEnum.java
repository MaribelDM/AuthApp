package com.microservicio.auth.entities;

import java.util.Arrays;
import java.util.Objects;

import org.bouncycastle.util.Strings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

  ADMIN(0, "ADMIN"),
  USER(1, "USER");

  
  private final Integer id;
  
  private final String name;

  public static RoleEnum of(Integer id) {
    return Arrays.stream(values())
      .filter(item -> item.getId().equals(id))
      .findFirst()
      .orElse(null);
  }

  public static RoleEnum of(String name) {
    return Arrays.stream(values())
      .filter(Objects::nonNull)
      .filter(item -> item.getName().equals(Strings.toUpperCase(name)))
      .findFirst()
      .orElse(null);
  }
}
