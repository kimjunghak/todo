package com.higherx.api.service.auth;

import com.higherx.api.config.UnAuthorizedException;
import com.higherx.api.model.dto.auth.JwtProperties;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private final JwtProperties jwtProperties;

    public String parseToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }
        return Strings.EMPTY;
    }

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .signWith(getSignedKey())
                .setIssuedAt(new Date())
                .compact();
    }

    public Long extractSubject(String token) {
        JwtParser parser = getJwtParser();

        return Long.parseLong(parser
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public Long checkAuth(HttpServletRequest request) {
        String token = parseToken(request);
        if (Strings.isEmpty(token) || !validateToken(token)) {
            throw new UnAuthorizedException("유효하지 않는 접근입니다.");
        }

        return extractSubject(token);
    }

    public void removeToken(String token) {
        //토큰 정보 삭제
    }

    public String refreshToken(String token) {
        Long userId = extractSubject(token);
        return generateToken(userId);
    }

    private boolean validateToken(String token) {
        try {
            getJwtParser()
            .parseClaimsJws(token)
            .getBody();

            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("Jwt Validate Error message: {}", e.getMessage(), e);
            return false;
        }
    }

    private Key getSignedKey() {
        String algorithm = SignatureAlgorithm.HS256.getJcaName();
        byte[] bytes = jwtProperties.getSecretKey().getBytes();
        return new SecretKeySpec(bytes, algorithm);
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build();
    }
}
