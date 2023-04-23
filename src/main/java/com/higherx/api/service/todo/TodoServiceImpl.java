package com.higherx.api.service.todo;

import com.higherx.api.config.UnAuthorizedException;
import com.higherx.api.model.dto.todo.TodoFront;
import com.higherx.api.model.dto.todo.TodoMapper;
import com.higherx.api.model.dto.todo.TodoState;
import com.higherx.api.model.entity.todo.Todo;
import com.higherx.api.model.entity.user.HigherxUser;
import com.higherx.api.repo.todo.TodoRepository;
import com.higherx.api.service.user.HigherxUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final HigherxUserService higherxUserService;

    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    @Override
    public Map<String, Object> getTodoList(TodoState state, Pageable pageable) {
        ArrayList<TodoState> todoStates = new ArrayList<>();
        if (state.equals(TodoState.ALL)) {
            todoStates.addAll(List.of(TodoState.COMPLETE, TodoState.INCOMPLETE));
        } else {
            todoStates.add(state);
        }
        Page<Todo> todoPage = todoRepository.findAllByStateIn(todoStates, pageable);

        List<Todo> todoList = todoPage.getContent();
        // Front 전달용 데이터로 전환
        List<TodoFront.ListInfo> listListInfo = todoMapper.listFromEntity(todoList);
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", listListInfo);
        return data;
    }

    @Override
    public void addTodo(Long userId, TodoFront.NewTodo newTodo) {
        Todo todo = todoMapper.toEntity(newTodo);
        HigherxUser higherxUser = higherxUserService.getHigherxUser(userId);
        todo.mapUser(higherxUser);
        todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = getTodo(todoId);
        checkRegister(userId, todo);
        todoRepository.deleteById(todoId);
    }

    @Override
    public void modifyTodo(Long userId, TodoFront.TodoInfo todoInfo) {
        Long todoId = todoInfo.getId();
        if (todoId == null) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        Todo todo = getTodo(todoId);
        checkRegister(userId, todo);
        todo.modify(todoInfo.getName(), todoInfo.getDescription(), TodoState.valueOf(todoInfo.getState().toUpperCase()));
        todoRepository.save(todo);
    }

    @Override
    public TodoFront.TodoInfo getTodoInfo(Long todoId) {
        Todo todo = getTodo(todoId);
        return todoMapper.todoInfoFromEntity(todo);
    }

    public Todo getTodo(Long todoId) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (todoOptional.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 Todo 입니다.");
        }
        return todoOptional.get();
    }

    private static void checkRegister(Long userId, Todo todo) {
        if (!userId.equals(todo.getHigherxUser().getId())) {
            throw new UnAuthorizedException("유효하지 않는 접근입니다.");
        }
    }
}
