package org.pe.llantatech.keycloakservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pe.llantatech.keycloakservice.dto.LoginRequestDto;
import org.pe.llantatech.keycloakservice.dto.LoginResponseDto;
import org.pe.llantatech.keycloakservice.service.impl.UserServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class KeycloakServiceApplicationTests {
    @Mock
    private RestTemplate restTemplate;  // Mock RestTemplate

    @InjectMocks
    private UserServiceImpl userServiceImpl;  // Instancia de la clase a testear

    private String loginUrl = "http://localhost:8080/realms/test/protocol/openid-connect/token";
    private String clientId = "test-client";
    private String clientSecret = "test-secret";
    private String grantType = "password";

    @BeforeEach
    void setUp() {
        // Iniciamos la configuración de los valores para la prueba
        userServiceImpl = new UserServiceImpl(restTemplate);
    }

    @Test
    void testLogin_Success() {
        // 🔹 ARRANGE
        LoginRequestDto request = new LoginRequestDto("john_doe", "securePassword123");

        // Creamos el objeto esperado para la respuesta
        LoginResponseDto expectedResponse = new LoginResponseDto();
        expectedResponse.setAccess_token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        expectedResponse.setRefresh_token("dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...");
        expectedResponse.setExpires_in("3600");
        expectedResponse.setRefresh_expires_in("7200");
        expectedResponse.setToken_type("Bearer");

        // Mock de la respuesta de RestTemplate
        ResponseEntity<LoginResponseDto> mockResponse = ResponseEntity.status(OK).body(expectedResponse);

        when(restTemplate.postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class)))
            .thenReturn(mockResponse);

        // 🔹 ACT
        LoginResponseDto actualResponse = userServiceImpl.login(request);

        // 🔹 ASSERT
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getAccess_token(), actualResponse.getAccess_token());
        assertEquals(expectedResponse.getRefresh_token(), actualResponse.getRefresh_token());
        assertEquals(expectedResponse.getExpires_in(), actualResponse.getExpires_in());
        assertEquals(expectedResponse.getRefresh_expires_in(), actualResponse.getRefresh_expires_in());
        assertEquals(expectedResponse.getToken_type(), actualResponse.getToken_type());
        verify(restTemplate, times(1))
            .postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class));
    }

    @Test
    void testLogin_Failure() {
        // 🔹 ARRANGE
        LoginRequestDto request = new LoginRequestDto("john_doe", "wrongPassword");

        // Mock de la respuesta de RestTemplate con un error (401)
        ResponseEntity<LoginResponseDto> mockResponse = ResponseEntity.status(401).body(null);

        when(restTemplate.postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class)))
            .thenReturn(mockResponse);

        // 🔹 ACT & ASSERT
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServiceImpl.login(request);
        });

        assertEquals("Authentication failed: 401 UNAUTHORIZED", thrown.getMessage());
        verify(restTemplate, times(1))
            .postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class));
    }

    @Test
    void testRefreshToken_Success() {
        // 🔹 ARRANGE
        String refreshToken = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...";
        LoginResponseDto expectedResponse = new LoginResponseDto();
        expectedResponse.setAccess_token("new-access-token");
        expectedResponse.setRefresh_token("new-refresh-token");
        expectedResponse.setExpires_in("3600");
        expectedResponse.setRefresh_expires_in("7200");
        expectedResponse.setToken_type("Bearer");

        // Mock de la respuesta de RestTemplate
        ResponseEntity<LoginResponseDto> mockResponse = ResponseEntity.status(OK).body(expectedResponse);

        when(restTemplate.postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class)))
            .thenReturn(mockResponse);

        // 🔹 ACT
        LoginResponseDto actualResponse = userServiceImpl.refreshToken(refreshToken);

        // 🔹 ASSERT
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getAccess_token(), actualResponse.getAccess_token());
        assertEquals(expectedResponse.getRefresh_token(), actualResponse.getRefresh_token());
        assertEquals(expectedResponse.getExpires_in(), actualResponse.getExpires_in());
        assertEquals(expectedResponse.getRefresh_expires_in(), actualResponse.getRefresh_expires_in());
        assertEquals(expectedResponse.getToken_type(), actualResponse.getToken_type());
        verify(restTemplate, times(1))
            .postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class));
    }

    @Test
    void testRefreshToken_Failure() {
        // 🔹 ARRANGE
        String refreshToken = "invalid-refresh-token";
        ResponseEntity<LoginResponseDto> mockResponse = ResponseEntity.status(400).body(null);

        when(restTemplate.postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class)))
            .thenReturn(mockResponse);

        // 🔹 ACT & ASSERT
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userServiceImpl.refreshToken(refreshToken);
        });

        assertEquals("Token refresh failed: 400 BAD_REQUEST", thrown.getMessage());
        verify(restTemplate, times(1))
            .postForEntity(eq(loginUrl), any(HttpEntity.class), eq(LoginResponseDto.class));
    }
}
