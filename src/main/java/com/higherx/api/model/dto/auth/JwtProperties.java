package com.higherx.api.model.dto.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secretKey;

    private final Long expiredTime;
}
