package org.pe.llantatech.keycloakservice.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.pe.llantatech.keycloakservice.dto.LoginRequestDto;
import org.pe.llantatech.keycloakservice.dto.LoginResponseDto;
import org.pe.llantatech.keycloakservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {UserServiceImpl.class, UserServiceImplIntegrationTest.TestConfig.class})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableWireMock({
        @ConfigureWireMock(
                name = "wiremock",
                port = 8089)
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplIntegrationTest {

    @InjectWireMock
    private WireMockServer wireMockServer;

    @Autowired
    private UserService userService;

    @BeforeEach
    void ensureWireMockIsRunning() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
        wireMockServer.resetAll();
    }


    @Test
    @Order(1)
    void whenValidLogin_thenReturnsAccessToken() {
        // 🔹 Arrange
        wireMockServer.stubFor(post(urlEqualTo("/realms/test/protocol/openid-connect/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("""
                            {
                              "access_token": "abc123",
                              "refresh_token": "refresh123"
                            }
                        """)
                        .withHeader("Content-Type", "application/json")));

        LoginRequestDto request = new LoginRequestDto("user", "pass");

        // 🔹 Act
        LoginResponseDto response = userService.login(request);

        // 🔹 Assert
        assertThat(response.getAccess_token()).isEqualTo("abc123");
        assertThat(response.getRefresh_token()).isEqualTo("refresh123");
    }

    @Test
    @Order(2)
    void whenLoginFails_thenThrowsRuntimeException() {
        // 🔹 Arrange
        wireMockServer.stubFor(post(urlEqualTo("/realms/test/protocol/openid-connect/token"))
                .willReturn(aResponse().withStatus(401)));

        LoginRequestDto request = new LoginRequestDto("user", "wrongpass");

        // 🔹 Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(request));
        assertThat(exception.getMessage()).contains("401 Unauthorized on POST request for");
    }

    @Test
    @Order(3)
    void whenRefreshTokenValid_thenReturnsNewAccessToken() {
        // 🔹 Arrange
        wireMockServer.stubFor(post(urlEqualTo("/realms/test/protocol/openid-connect/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("""
                            {
                              "access_token": "newAccess",
                              "refresh_token": "newRefresh"
                            }
                        """)
                        .withHeader("Content-Type", "application/json")));

        // 🔹 Act
        LoginResponseDto response = userService.refreshToken("validRefresh");

        // 🔹 Assert
        assertThat(response.getAccess_token()).isEqualTo("newAccess");
        assertThat(response.getRefresh_token()).isEqualTo("newRefresh");
    }

    @Test
    @Order(4)
    void whenRefreshTokenInvalid_thenThrowsException() {
        // 🔹 Arrange
        wireMockServer.stubFor(post(urlEqualTo("/realms/test/protocol/openid-connect/token"))
                .willReturn(aResponse().withStatus(400)));

        // 🔹 Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.refreshToken("invalidRefresh"));
        assertThat(exception.getMessage()).contains("400 Bad Request on POST request for");
    }

    @Test
    @Order(5)
    void whenServerDown_thenThrowsConnectionError() {
        // 🔹 Arrange
        wireMockServer.stop(); // simulate server down
        LoginRequestDto request = new LoginRequestDto("user", "pass");

        // 🔹 Act & Assert
        assertThrows(Exception.class, () -> userService.login(request));

        wireMockServer.start(); // restore for next tests
    }

    // ⬇️ Inject restTemplate & override login URL
    @Configuration
    static class TestConfig {

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }

        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${wiremock.server.baseUrl}") String wireMockBaseUrl) {
            return builder.rootUri(wireMockBaseUrl).build();
        }
    }
}
