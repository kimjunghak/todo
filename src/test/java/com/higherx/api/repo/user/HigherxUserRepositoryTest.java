package com.higherx.api.repo.user;

import com.higherx.api.model.entity.user.HigherxUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class HigherxUserRepositoryTest {

    @Autowired
    HigherxUserRepository higherxUserRepository;

    @BeforeEach
    void setUp() {
        ArrayList<HigherxUser> higherxUsers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String strIndex = String.valueOf(i);
            HigherxUser higherxUser = new HigherxUser((long) i, "Account ".concat(strIndex), "Password ".concat(strIndex), "Nickname ".concat(strIndex), "Phone ".concat(strIndex), "Crn ".concat(strIndex), null);
            higherxUsers.add(higherxUser);
        }
        higherxUserRepository.saveAll(higherxUsers);
    }

    @Test
    @DisplayName("계정으로 찾기")
    void findByAccount() {
        String account = "Account 1";
        Optional<HigherxUser> higherxUser = higherxUserRepository.findByAccount(account);

        assertTrue(higherxUser.isPresent());
        assertEquals(higherxUser.get().getAccount(), account);
    }

    @Test
    @DisplayName("사업자 번호로 찾기")
    void findByCrn() {
        String crn = "Crn 1";
        Optional<HigherxUser> higherxUser = higherxUserRepository.findByCrn(crn);

        assertTrue(higherxUser.isPresent());
        assertEquals(higherxUser.get().getCrn(), crn);
    }

    @Test
    @DisplayName("닉네임으로 찾기")
    void findByNickname() {
        String nickname = "Nickname 1";
        Optional<HigherxUser> higherxUser = higherxUserRepository.findByNickname(nickname);

        assertTrue(higherxUser.isPresent());
        assertEquals(higherxUser.get().getNickname(), nickname);
    }

    @Test
    @DisplayName("모두 찾기")
    void findAll() {
        List<HigherxUser> higherxUsers = higherxUserRepository.findAll();

        assertEquals(higherxUsers.size(), 10);
    }
}