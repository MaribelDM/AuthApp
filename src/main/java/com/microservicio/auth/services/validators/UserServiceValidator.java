package com.microservicio.auth.services.validators;

import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.microservicio.auth.config.CustomPasswordEncoder;
import com.microservicio.auth.dtos.NuevaContraseñaRequest;
import com.microservicio.auth.dtos.SignUpDTO;
import com.microservicio.auth.entities.RoleEnum;
import com.microservicio.auth.entities.User;
import com.microservicio.auth.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceValidator {

  private final UserRepository userRepository;
  

  private final CustomPasswordEncoder customPasswordEncoder;

  public void validateSignUp(SignUpDTO signUpDto) throws ValidationException {

		String username = signUpDto.getUsername();
		String role = signUpDto.getRole();

		Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsernameIgnoreCase(username));

		if (userOpt.isPresent()) {
			throw new DataIntegrityViolationException(String.format("El nombre %s ya esta en uso", username));
		}

		if (Objects.isNull(RoleEnum.of(role))) {
			throw new ValidationException(String.format("El rol %s que especifica no se puede encontrar", role));
		}

		passwordMatch(signUpDto.getPassword(), signUpDto.getPasswordVerification());
	}

	private void passwordMatch(String password, String verificationPassword) throws ValidationException {
		if (!password.equals(verificationPassword)) {
			throw new ValidationException("Las contraseñas no coinciden");
		}
	}
  
	public void validacionActualizarContraseña(User loggedUser,
			NuevaContraseñaRequest updatePasswordWithVerificationCodeDTO) throws ValidationException {

		passwordMatch(updatePasswordWithVerificationCodeDTO.getNuevaContraseña(),
				updatePasswordWithVerificationCodeDTO.getVerificacionNuevaContraseña());

		validarNuevaContraseña(loggedUser, updatePasswordWithVerificationCodeDTO.getNuevaContraseña());
	}

	private void validarNuevaContraseña(User usuarioActual, String contraseñaAActualizar) throws ValidationException {

		if (customPasswordEncoder.matches(contraseñaAActualizar, usuarioActual.getPasswordHash())) {
			throw new ValidationException("La nueva contraseña no debe ser la misma la actual");
		}
	}
}
