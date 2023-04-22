package com.higherx.api.repo.user;

import com.higherx.api.model.entity.user.HigherxUser;
import com.higherx.api.repo.BaseRepository;

import java.util.Optional;

public interface HigherxUserRepository extends BaseRepository<HigherxUser, Long> {

    Optional<HigherxUser> findByAccount(String account);

    Optional<HigherxUser> findByCrn(String crn);

    Optional<HigherxUser> findByNickname(String nickname);
}
