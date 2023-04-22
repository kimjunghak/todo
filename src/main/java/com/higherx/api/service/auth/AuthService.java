package com.higherx.api.service.auth;

import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserFront;

public interface AuthService {

    AuthInfo login(HigherxUserFront.Signup signup);

    void logout(AuthInfo authInfo);

    AuthInfo refreshToken(AuthInfo refreshToken);
}
