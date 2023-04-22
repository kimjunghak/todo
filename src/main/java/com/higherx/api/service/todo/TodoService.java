package com.higherx.api.service.todo;

import com.higherx.api.model.dto.todo.TodoFront;
import com.higherx.api.model.dto.todo.TodoState;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface TodoService {

    Map<String, Object> getTodoList(TodoState state, Pageable pageable);

    void addTodo(Long userId, TodoFront.NewTodo newTodo);

    /**
     * 다른 유저의 Todo를 삭제할 수 없어야 합니다.
     * @param todoId
     */
    void deleteTodo(Long userId, Long todoId);

    void modifyTodo(Long userId, TodoFront.TodoInfo todoInfo);

    TodoFront.TodoInfo getTodoInfo(Long todoId);
}
