package org.pe.llantatech.keycloakservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 06/05/25 @ 17:03
 */
@Schema(description = "DTO representing a request to refresh an access token")
public record RefreshTokenRequestDto(
        @Schema(description = "The refresh token provided to the user", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
        String refreshToken
) {}