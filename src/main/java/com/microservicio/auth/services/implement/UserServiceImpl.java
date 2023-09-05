package com.microservicio.auth.services.implement;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.microservicio.auth.config.CustomPasswordEncoder;
import com.microservicio.auth.dtos.NuevaContraseñaRequest;
import com.microservicio.auth.dtos.SignUpDTO;
import com.microservicio.auth.entities.RoleEnum;
import com.microservicio.auth.entities.User;
import com.microservicio.auth.repositories.UserRepository;
import com.microservicio.auth.services.UserService;
import com.microservicio.auth.services.validators.UserServiceValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService , UserDetailsService{

  private final CustomPasswordEncoder customPasswordEncoder;
  private final UserRepository userRepository;
  private final UserServiceValidator userServiceValidator;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return Optional.ofNullable(userRepository.findByUsernameIgnoreCase(username))
      .map(user -> {
        if (!user.isEnabled()) {
          throw new UsernameNotFoundException(String.format("Usuario no encontrado con el nombre %s", username));
        }
        return user;
      })
      .orElseThrow(() -> new UsernameNotFoundException(String.format("Usuario no encontrado con el nombre %s", username)));
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public User signUp(SignUpDTO signUpDto, boolean isFirstRegister) throws ValidationException {

    userServiceValidator.validateSignUp(signUpDto);

    final RoleEnum role = RoleEnum.of(signUpDto.getRole());
    final String username = signUpDto.getUsername();

    User user = User.builder()
      .username(username)
      .name(signUpDto.getName())
      .passwordHash(customPasswordEncoder.encode(signUpDto.getPassword()))
      .role(role.getId())
      .build();

    return userRepository.save(user);
  }

  @Override
  public User find(String username) {
    return Optional.ofNullable(userRepository.findByUsernameIgnoreCase(username))
      .orElseThrow(() -> new UsernameNotFoundException(String.format("Usuario no encontrado con el nombre %s", username)));
  }

	@Override
	public void actualizarContraseña(NuevaContraseñaRequest nuevaContraseñaRequest) throws ValidationException {

		User user = find(nuevaContraseñaRequest.getNombreUsuario());

		userServiceValidator.validacionActualizarContraseña(user, nuevaContraseñaRequest);

		user.setPasswordHash(customPasswordEncoder.encode(nuevaContraseñaRequest.getNuevaContraseña()));
		userRepository.save(user);
	}
}
