package com.higherx.api.model.dto.user;

public record Verify(boolean verify) {

    public static Verify exist() {
        return new Verify(true);
    }

    public static Verify nonExist() {
        return new Verify(false);
    }
}
