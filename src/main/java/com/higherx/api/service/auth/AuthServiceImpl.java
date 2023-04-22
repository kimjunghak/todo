package com.higherx.api.service.auth;

import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.service.user.HigherxUserService;
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
    public AuthInfo login(HigherxUserFront.Signup signup) {
        Long userId = higherxUserService.signup(signup);
        String accessToken = jwtService.generateToken(userId);
        return new AuthInfo(accessToken);
    }

    @Override
    public void logout(AuthInfo authInfo) {
        String token = authInfo.accessToken();
        jwtService.removeToken(token);
    }

    @Override
    public AuthInfo refreshToken(AuthInfo refreshToken) {
        String prevToken = refreshToken.accessToken();
        String newToken = jwtService.refreshToken(prevToken);
        return new AuthInfo(newToken);
    }
}
