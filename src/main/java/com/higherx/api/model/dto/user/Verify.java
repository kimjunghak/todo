package com.higherx.api.model.dto.user;

public record Verify(boolean verify) {

    public static Verify available() {
        return new Verify(true);
    }

    public static Verify unavailable() {
        return new Verify(false);
    }
}
