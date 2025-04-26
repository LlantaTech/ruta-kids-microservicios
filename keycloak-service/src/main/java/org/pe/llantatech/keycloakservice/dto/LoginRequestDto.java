package org.pe.llantatech.keycloakservice.dto;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/26/25 @ 16:45
 */
public record LoginRequestDto (
        String username,
        String password
){
}
