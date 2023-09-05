package com.microservicio.auth.services;

import javax.xml.bind.ValidationException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.microservicio.auth.dtos.NuevaContraseñaRequest;
import com.microservicio.auth.dtos.SignUpDTO;
import com.microservicio.auth.entities.User;

public interface UserService extends UserDetailsService{

  User signUp(SignUpDTO signUpDto, boolean isFirstRegister) throws ValidationException;

  User find(String username);

  void actualizarContraseña(NuevaContraseñaRequest updatePasswordWithVerificationCodeDTO)
		throws ValidationException;
}
