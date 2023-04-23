package com.higherx.api.model.entity.user;

import com.higherx.api.model.entity.BaseEntity;
import com.higherx.api.model.entity.todo.Todo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity @Table
public class HigherxUser extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    private String password;

    private String nickname;

    private String phone;

    private String crn;

    @OneToMany(mappedBy = "higherxUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Todo> todo;

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void signout() {
        this.password = Strings.EMPTY;
        this.phone = Strings.EMPTY;
        this.crn = Strings.EMPTY;
    }
}
