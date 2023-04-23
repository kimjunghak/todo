package com.higherx.api.service.auth;

import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.service.user.HigherxUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final JwtService jwtService;
    private final HigherxUserService higherxUserService;

    @Override
    public AuthInfo login(HigherxUserFront.Login login) {
        Long userId = higherxUserService.login(login);
        String accessToken = jwtService.generateToken(userId);
        return new AuthInfo(accessToken);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtService.parseToken(request);
        jwtService.removeToken(token);
    }

    @Override
    public AuthInfo refreshToken(HttpServletRequest request) {
        String refreshToken = jwtService.parseToken(request);
        String newToken = jwtService.refreshToken(refreshToken);
        return new AuthInfo(newToken);
    }
}
