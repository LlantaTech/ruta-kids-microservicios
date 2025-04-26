package org.pe.llantatech.keycloakservice.service;

import org.pe.llantatech.keycloakservice.dto.LoginRequestDto;
import org.pe.llantatech.keycloakservice.dto.LoginResponseDto;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/26/25 @ 16:47
 */
public interface UserService {

    LoginResponseDto login(LoginRequestDto request);
}
