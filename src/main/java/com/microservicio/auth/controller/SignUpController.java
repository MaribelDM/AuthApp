package com.microservicio.auth.controller;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.auth.dtos.NuevaContraseñaRequest;
import com.microservicio.auth.dtos.SignUpDTO;
import com.microservicio.auth.dtos.UserDTO;
import com.microservicio.auth.entities.User;
import com.microservicio.auth.mappers.UserDTOMapper;
import com.microservicio.auth.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(SignUpController.SIGN_UP_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignUpController {

  public static final String SIGN_UP_CONTROLLER_PREFIX = "/sign-up";

  private final UserService userService;
  private final UserDTOMapper userDTOMapper;

  @PostMapping
  public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDto) throws ValidationException {
    User newUser = userService.signUp(signUpDto, true);
    return ResponseEntity.ok(userDTOMapper.map(newUser));
  }
  
	@PostMapping("actualizar-contrasenia")
	public ResponseEntity<Void> actualizarContraseña(
			@RequestBody NuevaContraseñaRequest updatePasswordWithVerificationCodeDTO) throws ValidationException {
		userService.actualizarContraseña(updatePasswordWithVerificationCodeDTO);
		return ResponseEntity.ok().build();
	}

}

