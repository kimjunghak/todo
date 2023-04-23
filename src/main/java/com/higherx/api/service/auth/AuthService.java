package com.higherx.api.service.auth;

import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserFront;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    AuthInfo login(HigherxUserFront.Login login);

    void logout(HttpServletRequest request);

    AuthInfo refreshToken(HttpServletRequest request);
}
