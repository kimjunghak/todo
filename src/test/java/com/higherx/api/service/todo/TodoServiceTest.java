package com.higherx.api.service.todo;

import com.higherx.api.model.dto.todo.TodoFront;
import com.higherx.api.model.dto.todo.TodoMapperImpl;
import com.higherx.api.model.dto.todo.TodoState;
import com.higherx.api.model.entity.todo.Todo;
import com.higherx.api.model.entity.user.HigherxUser;
import com.higherx.api.repo.todo.TodoRepository;
import com.higherx.api.service.user.HigherxUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class TodoServiceTest {

    TodoService todoService;

    @MockBean
    HigherxUserService higherxUserService;

    @MockBean
    TodoRepository todoRepository;

    HigherxUser higherxUser;

    @BeforeEach
    void setUp() {
        todoService = new TodoServiceImpl(higherxUserService, todoRepository, new TodoMapperImpl());

        higherxUser = new HigherxUser(1L, "Account", "Password", "Nickname", "Phone", "Crn", new ArrayList<>());
    }

    @Test
    @DisplayName("미완료된 할일 목록 조회")
    void incompleteList() {
        ArrayList<Todo> todos = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String strIndex = String.valueOf(i);
            TodoState state = TodoState.COMPLETE;
            if (i % 2 == 0) {
                state = TodoState.INCOMPLETE;
            }
            Todo todo = new Todo((long) i, "Todo ".concat(strIndex), state, "Todo ".concat(strIndex).concat("description"), null);
            todos.add(todo);
        }
        Pageable pageable = Mockito.mock(Pageable.class);

        List<Todo> incomplete = todos.stream().filter(todo -> todo.getState().equals(TodoState.INCOMPLETE)).toList();
        PageImpl<Todo> incompletePage = new PageImpl<>(incomplete);

        Mockito.when(todoRepository.findAllByStateIn(List.of(TodoState.INCOMPLETE), pageable)).thenReturn(incompletePage);

        Map<String, Object> todoList = todoService.getTodoList(TodoState.INCOMPLETE, pageable);

        List items = (List) todoList.get("items");
        assertEquals(items.size(), 7);
    }

    @Test
    @DisplayName("완료된 할일 목록 조회")
    void completeList() {
        ArrayList<Todo> todos = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String strIndex = String.valueOf(i);
            TodoState state = TodoState.COMPLETE;
            if (i % 2 == 0) {
                state = TodoState.INCOMPLETE;
            }
            Todo todo = new Todo((long) i, "Todo ".concat(strIndex), state, "Todo ".concat(strIndex).concat("description"), null);
            todos.add(todo);
        }
        Pageable pageable = Mockito.mock(Pageable.class);

        List<Todo> complete = todos.stream().filter(todo -> todo.getState().equals(TodoState.COMPLETE)).toList();
        PageImpl<Todo> completePage = new PageImpl<>(complete);

        Mockito.when(todoRepository.findAllByStateIn(List.of(TodoState.COMPLETE), pageable)).thenReturn(completePage);

        Map<String, Object> todoList = todoService.getTodoList(TodoState.COMPLETE, pageable);

        List items = (List) todoList.get("items");
        assertEquals(items.size(), 8);
    }

    @Test
    @DisplayName("할일 추가")
    void addTodo() {
        TodoFront.NewTodo newTodo = new TodoFront.NewTodo();
        newTodo.setName("Todo");
        newTodo.setDescription("Todo Description");

        Todo todo = new Todo(1L, "Todo", TodoState.INCOMPLETE, "Todo Description", higherxUser);
        Mockito.when(todoRepository.save(todo)).thenReturn(todo);

        todoService.addTodo(higherxUser.getId(), newTodo);

        assertEquals(todo.getId(), 1L);
    }

    @Test
    @DisplayName("할일 삭제")
    void deleteTodo() {
        Todo todo = new Todo(1L, "Todo", TodoState.INCOMPLETE, "Todo Description", higherxUser);
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.deleteTodo(higherxUser.getId(), 1L);
        Mockito.verify(todoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("할일 수정")
    void modifyTodo() {
        TodoFront.TodoInfo todoInfo = new TodoFront.TodoInfo();
        todoInfo.setId(1L);
        todoInfo.setName("Todo1");
        todoInfo.setDescription("Todo Description");
        todoInfo.setState(TodoState.COMPLETE.name());

        Todo todo = new Todo(1L, "Todo", TodoState.INCOMPLETE, "Todo Description", higherxUser);
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        todoService.modifyTodo(1L, todoInfo);

        assertEquals(todo.getState(), TodoState.COMPLETE);
        assertEquals(todo.getName(), "Todo1");
    }

    @Test
    @DisplayName("특정 할일 조회")
    void getTodoInfo() {
        Todo todo = new Todo(1L, "Todo", TodoState.INCOMPLETE, "Todo Description", higherxUser);
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        TodoFront.TodoInfo todoInfo = todoService.getTodoInfo(1L);

        assertEquals(todoInfo.getName(), todo.getName());
    }
}