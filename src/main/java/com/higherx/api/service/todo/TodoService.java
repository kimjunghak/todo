package com.higherx.api.service.todo;

import com.higherx.api.model.dto.todo.TodoDto;
import com.higherx.api.model.dto.todo.TodoState;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TodoService {

    Map<String, Object> getTodoList(TodoState state, Pageable pageable);

    void addTodo(TodoDto.NewTodo newTodo);

    /**
     * 다른 유저의 Todo를 삭제할 수 없어야 합니다.
     * @param todoId
     */
    void deleteTddo(Long todoId);

    void modifyTodo(TodoDto.TodoInfo todoInfo);

    void getTodoInfo(Long todoId);
}
