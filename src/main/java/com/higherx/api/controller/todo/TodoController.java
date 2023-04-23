package com.higherx.api.controller.todo;

import com.higherx.api.model.dto.todo.TodoFront;
import com.higherx.api.model.dto.todo.TodoState;
import com.higherx.api.service.auth.JwtService;
import com.higherx.api.service.todo.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final JwtService jwtService;
    private final TodoService todoService;

    @GetMapping("")
    public Map<String, Object> getTodoList(HttpServletRequest request,
                                           @RequestParam(required = false) String state,
                                           @RequestParam Integer size) {
        jwtService.checkAuth(request);
        return todoService.getTodoList(parseState(state), PageRequest.ofSize(size));
    }

    @PostMapping("")
    public void addTodo(HttpServletRequest request, @RequestBody TodoFront.NewTodo newTodo) {
        Long userId = jwtService.checkAuth(request);
        todoService.addTodo(userId, newTodo);
    }

    @DeleteMapping("")
    public void deleteTodo(HttpServletRequest request,
                           @RequestParam Long todoId) {
        Long userId = jwtService.checkAuth(request);
        todoService.deleteTodo(userId, todoId);
    }

    @PutMapping("")
    public void modifyTodo(HttpServletRequest request,
                           @RequestBody TodoFront.TodoInfo todoInfo) {
        Long userId = jwtService.checkAuth(request);
        todoService.modifyTodo(userId, todoInfo);
    }

    @GetMapping("/{todoId}")
    public TodoFront.TodoInfo getDetailInfo(HttpServletRequest request,
                                            @PathVariable Long todoId) {
        jwtService.checkAuth(request);
        return todoService.getTodoInfo(todoId);
    }

    private TodoState parseState(String state) {
        if (state == null) {
            return TodoState.ALL;
        }
        return TodoState.valueOf(state.toUpperCase());
    }
}
