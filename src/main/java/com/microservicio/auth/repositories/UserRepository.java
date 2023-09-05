package com.microservicio.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservicio.auth.entities.RoleEnum;
import com.microservicio.auth.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  	/**
	 * Busca username igual al string pasado por parametro ignorando las diferencias
	 * entre mayusculas y minusculas
	 * 
	 * @param username
	 * @return a user
	 */
  User findByUsernameIgnoreCase(String username);

  	/**
	 * Devolvera la lista de todos los usuarios con el mismo rol
	 * 
	 * @param role
	 * @return
	 */
  List<User> findByRole(RoleEnum role);
  
}
