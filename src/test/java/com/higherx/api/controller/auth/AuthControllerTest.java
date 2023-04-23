package com.higherx.api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = {AuthController.class})
class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        HigherxUserFront.Login login = new HigherxUserFront.Login();
        login.setAccount("Account");
        login.setPassword("Password");

        String accessToken = "Access Token";
        Mockito.when(authService.login(login)).thenReturn(new AuthInfo(accessToken));

        String requestBody = objectMapper.writeValueAsString(login);
        String content = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthInfo authInfo = objectMapper.readValue(content, AuthInfo.class);
        assertEquals(authInfo.accessToken(), accessToken);
    }

    @Test
    void logout() {
    }

    @Test
    void token() {
    }
}