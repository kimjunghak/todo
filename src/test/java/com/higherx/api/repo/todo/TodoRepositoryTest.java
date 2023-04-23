package com.higherx.api.repo.todo;

import com.higherx.api.model.dto.todo.TodoState;
import com.higherx.api.model.entity.todo.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        ArrayList<Todo> todos = new ArrayList<>();
        for (int i = 1; i <= 35; i++) {
            String strIndex = String.valueOf(i);
            TodoState state;
            if (i % 2 == 0) {
                state = TodoState.INCOMPLETE;
            } else {
                state = TodoState.COMPLETE;
            }
            Todo todo = new Todo((long) i, "Todo ".concat(strIndex), state, "Todo ".concat(strIndex).concat("description"), null);
            todos.add(todo);
        }
        todoRepository.saveAll(todos);
    }

    @Test
    @DisplayName("Complete 목록 조회")
    void getCompleteList() {
        Page<Todo> todoPage = todoRepository.findAllByStateIn(List.of(TodoState.COMPLETE), Pageable.ofSize(5));

        assertEquals(todoPage.getTotalElements(), 18);
        assertEquals(todoPage.getContent().size(), 5);
    }

    @Test
    @DisplayName("Incomplete 목록 조회")
    void getIncompleteList() {
        Page<Todo> todoPage = todoRepository.findAllByStateIn(List.of(TodoState.INCOMPLETE), Pageable.ofSize(5));

        assertEquals(todoPage.getTotalElements(), 17);
        assertEquals(todoPage.getContent().size(), 5);
    }

    @Test
    @DisplayName("전체 목록 조회")
    void getAllList() {
        Page<Todo> todoPage = todoRepository.findAllByStateIn(List.of(TodoState.COMPLETE, TodoState.INCOMPLETE), Pageable.ofSize(5));

        assertEquals(todoPage.getTotalElements(), 35);
        assertEquals(todoPage.getContent().size(), 5);
    }
}
