package org.pe.llantatech.keycloakservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/26/25 @ 16:45
 */
@Data
@Schema(description = "DTO representing the response after a successful login")
public class LoginResponseDto {

    @Schema(description = "Access token for the user", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String access_token;

    @Schema(description = "Refresh token for the user", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
    private String refresh_token;

    @Schema(description = "Time in seconds until the access token expires", example = "3600")
    private String expires_in;

    @Schema(description = "Time in seconds until the refresh token expires", example = "7200")
    private String refresh_expires_in;

    @Schema(description = "Type of the token issued", example = "Bearer")
    private String token_type;
}