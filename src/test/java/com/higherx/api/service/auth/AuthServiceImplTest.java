package com.higherx.api.service.auth;

import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.service.user.HigherxUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class AuthServiceImplTest {

    AuthService authService;

    @MockBean
    JwtService jwtService;

    @MockBean
    HigherxUserService higherxUserService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(jwtService, higherxUserService);
    }

    @Test
    @DisplayName("로그인")
    void login() {
        HigherxUserFront.Login login = new HigherxUserFront.Login();
        login.setAccount("Account 1");
        login.setPassword("Password 1");

        Mockito.when(higherxUserService.login(login)).thenReturn(1L);
        String accessToken = "Access Token 1";
        Mockito.when(jwtService.generateToken(1L)).thenReturn(accessToken);

        AuthInfo authInfo = authService.login(login);
        assertEquals(authInfo.accessToken(), accessToken);
    }

    @Test
    void logout() {
    }

    @Test
    void refreshToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String accessToken = "Access Token 1";
        Mockito.when(jwtService.parseToken(request)).thenReturn(accessToken);
        String refreshToken = "Refresh Token 1";
        Mockito.when(jwtService.refreshToken(accessToken)).thenReturn(refreshToken);
        AuthInfo authInfo = authService.refreshToken(request);

        assertEquals(authInfo.accessToken(), refreshToken);
    }
}