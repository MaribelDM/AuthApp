package com.microservicio.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class AuthApplication {

  public static void main(String[] args) {
    SpringApplicationBuilder app = new SpringApplicationBuilder(AuthApplication.class);
    app.build().addListeners(new ApplicationPidFileWriter()); // register PID write to spring boot. It will write PID to file
    app.run(args);
  }
}
