package com.higherx.api.repo.todo;

import com.higherx.api.model.dto.todo.TodoState;
import com.higherx.api.model.entity.todo.Todo;
import com.higherx.api.repo.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepository extends BaseRepository<Todo, Long> {

    Page<Todo> findAllByState(TodoState state, Pageable pageable);
}
