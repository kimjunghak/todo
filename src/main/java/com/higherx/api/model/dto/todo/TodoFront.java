package com.higherx.api.model.dto.todo;

import lombok.Data;

import java.time.LocalDateTime;

public class TodoFront {

    @Data
    public static class ListInfo {
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

    @Data
    public static class TodoInfo {
        private Long id;

        private String name;

        private String description;

        private String state;
    }
}
