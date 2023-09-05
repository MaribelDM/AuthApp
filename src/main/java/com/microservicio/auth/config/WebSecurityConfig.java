package com.microservicio.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.microservicio.auth.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Servicio busqueda de usuario
   */
	@Autowired
  private UserService userService;
  /**
   * Criptador de clave 
   */
  @Autowired
  private CustomPasswordEncoder customPasswordEncoder;

  @Override
  @Autowired
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(userService)
      .passwordEncoder(customPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http
////      .cors().disable()
//      .csrf().disable()
////      .anonymous().disable()
//      .authorizeRequests()
//      .antMatchers("/auth/oauth/token").permitAll().antMatchers("/**").authenticated()
//      .and().httpBasic();
    
	http.cors().disable().requestMatchers()
    .antMatchers("/oauth/authorize").and().authorizeRequests()
	.anyRequest().permitAll().and()
	//mantega sesion abierta y para quitar la validacion de csrf(cors r filter)
	.csrf().disable()
	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  

//    http.headers().frameOptions().disable();
//	  http.authorizeRequests()
//		.anyRequest().authenticated()
//		.and()
//		.csrf().disable()
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
