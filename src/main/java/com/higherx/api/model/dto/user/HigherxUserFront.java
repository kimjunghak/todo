package com.higherx.api.model.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

public class HigherxUserFront {

    @Data
    public static class UserInfo {
        private String nickname;

        private String phone;

        private String crn;

        private LocalDateTime createdAt;
    }

    @Data
    public static class Signup {
        private String account;

        private String password;

        private String nickname;

        private String phone;

        private String crn;
    }
}
