package com.higherx.api.service.user;

import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.model.dto.user.Verify;
import com.higherx.api.model.entity.user.HigherxUser;

public interface HigherxUserService {

    void signup(HigherxUserFront.Signup signup);

    Long login(HigherxUserFront.Login login);

    HigherxUserFront.UserInfo getInfo(Long higherxUserId);

    /**
     * 회원 탈퇴시 실제 Database에서 데이터를 삭제하는 것이 아닌
     * `phone`, `crn`, `password`를 마스킹하고 로그인 할 수 없도록 합니다.
     */
    void signout(Long higherxUserId);

    HigherxUser getHigherxUser(Long higherxUserId);

    Verify verifyAccount(String account);

    Verify verifyCrn(String crn);

    Verify verifyNickname(String nickname);
}
