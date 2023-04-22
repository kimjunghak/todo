package com.higherx.api.model.entity.todo;

import com.higherx.api.model.dto.todo.TodoState;
import com.higherx.api.model.entity.BaseEntity;
import com.higherx.api.model.entity.user.HigherxUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity @Table
public class Todo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TodoState state = TodoState.INCOMPLETE;

    private String description;

    @OneToOne
    @JoinColumn(name = "userId")
    private HigherxUser higherxUser;

    public void mapUser(HigherxUser higherxUser) {
        this.higherxUser = higherxUser;
    }

    public void modify(String name, String description, TodoState state) {
        this.name = name;
        this.description = description;
        this.state = state;
    }
}
