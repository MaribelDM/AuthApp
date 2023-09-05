package com.microservicio.auth.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//ESTA CLASE CONFIGURA EL CORS PARA QUE NO DE ERROR EN LA PETICION EN ANGULAR
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  public static final String OPEN_ENDPOINTS_MATCHER = "**";

  @Override
  public void configure(HttpSecurity http) throws Exception {
	  
	  
		http.cors().configurationSource(corsConfigurationSource()).and().requestMatchers().antMatchers("/v1/**").and()
				.authorizeRequests().antMatchers(OPEN_ENDPOINTS_MATCHER).permitAll().and().sessionManagement()
				;
  }
  
  @Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*"));
		config.setAllowedMethods(Arrays.asList("GET","POST","PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		
		UrlBasedCorsConfigurationSource basedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		basedCorsConfigurationSource.registerCorsConfiguration("/**", config);
		
		return basedCorsConfigurationSource;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> filter = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filter;
	}
}

