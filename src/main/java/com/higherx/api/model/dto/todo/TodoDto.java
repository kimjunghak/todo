package com.higherx.api.model.dto.todo;

import lombok.Data;

import java.time.LocalDateTime;

public class TodoDto {

    @Data
    public static class TodoInfo {
        private Long id;

        private String name;

        private TodoState state;

        private LocalDateTime createdAt;
    }

    @Data
    public static class NewTodo {
        private String name;

        private String description;
    }
}
