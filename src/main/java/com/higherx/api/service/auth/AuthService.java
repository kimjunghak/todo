package com.higherx.api.service.auth;

import com.higherx.api.model.dto.auth.AuthInfo;
import com.higherx.api.model.dto.user.HigherxUserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthInfo login(HigherxUserDto.Signup signup);

    void logout(AuthInfo authInfo);

    AuthInfo refreshToken(AuthInfo refreshToken);
}
