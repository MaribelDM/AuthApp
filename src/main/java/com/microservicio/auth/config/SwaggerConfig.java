package com.microservicio.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //Habilita swagger API
public class SwaggerConfig{
	
	//Create Docket object for swagger documentation type to enable documentation 
	//for the specified base package and build the API.
	 @Bean
	    public Docket productApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	            .select()
	            .apis(RequestHandlerSelectors.basePackage("com.microservicio.auth.resources"))
	            .paths(PathSelectors.ant("/**"))
	            .build()
	            .apiInfo(apiInfo());
	    }
	
	private ApiInfo apiInfo() {
		Contact contact = new Contact("", "", "");
        return new ApiInfoBuilder()
                .title("Spring REST Swagger Autorizacion ")
                .description("Aplicacion autentificacion")
                .contact(contact)
                .version("1.0")
                .build();
    }

}