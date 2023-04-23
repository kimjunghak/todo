package com.higherx.api.model.dto.todo;

import com.higherx.api.model.entity.todo.Todo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Named("listInfo")
    TodoFront.ListInfo listInfoFromEntity(Todo todo);

    @IterableMapping(qualifiedByName = "listInfo")
    List<TodoFront.ListInfo> listFromEntity(List<Todo> todoList);

    TodoFront.TodoInfo todoInfoFromEntity(Todo todo);

    Todo toEntity(TodoFront.NewTodo newTodo);
}
