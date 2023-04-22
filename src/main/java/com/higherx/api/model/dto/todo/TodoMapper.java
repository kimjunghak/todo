package com.higherx.api.model.dto.todo;

import com.higherx.api.model.entity.todo.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    TodoFront.ListInfo listInfoFromEntity(Todo todo);

    TodoFront.TodoInfo todoInfoFromEntity(Todo todo);

    Todo toEntity(TodoFront.NewTodo newTodo);
}
