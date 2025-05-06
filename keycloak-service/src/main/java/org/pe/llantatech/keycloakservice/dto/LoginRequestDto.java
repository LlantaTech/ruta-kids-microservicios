package org.pe.llantatech.keycloakservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/26/25 @ 16:45
 */
@Schema(description = "DTO representing a login request with username and password")
public record LoginRequestDto (
        @NotBlank
        @Schema(description = "The username of the user", example = "john_doe")
        String username,

        @NotBlank
        @Schema(description = "The password of the user", example = "securePassword123")
        String password
){
}