package com.microservicio.auth.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class NuevaContraseñaRequest {

    
    private String nombreUsuario;

    private String nuevaContraseña;

    private String verificacionNuevaContraseña;

}
