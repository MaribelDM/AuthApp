package com.microservicio.auth.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservicio.auth.dtos.UserDTO;
import com.microservicio.auth.entities.RoleEnum;
import com.microservicio.auth.entities.User;

@Component
public class UserDTOMapper {

  @Autowired
  private ModelMapper modelMapper;


	public UserDTO map(User user) {
		UserDTO userout = modelMapper.map(user, UserDTO.class);
		userout.setRole(RoleEnum.of(Integer.valueOf(user.getRole().toString())).getName());
		return userout;
	}

}
