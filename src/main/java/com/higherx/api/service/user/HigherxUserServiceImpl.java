package com.higherx.api.service.user;

import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.model.dto.user.HigherxUserMapper;
import com.higherx.api.model.dto.user.Verify;
import com.higherx.api.model.entity.user.HigherxUser;
import com.higherx.api.repo.user.HigherxUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HigherxUserServiceImpl implements HigherxUserService{

    private final HigherxUserRepository higherxUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final HigherxUserMapper higherxUserMapper;

    @Override
    public Long signup(HigherxUserFront.Signup signup) {
        HigherxUser higherxUser = higherxUserMapper.toEntity(signup);
        setEncodePassword(higherxUser);
        higherxUserRepository.save(higherxUser);
        return higherxUser.getId();
    }

    @Override
    public HigherxUserFront.UserInfo getInfo(Long higherxUserId) {
        HigherxUser higherxUser = getHigherxUser(higherxUserId);
        return higherxUserMapper.userInfoFromEntity(higherxUser);
    }

    @Override
    public void signout(Long higherxUserId) {
        HigherxUser higherxUser = getHigherxUser(higherxUserId);
        higherxUser.signout(); //마스킹 처리
        higherxUserRepository.save(higherxUser);
    }

    @Override
    public HigherxUser getHigherxUser(Long higherxUserId) {
        Optional<HigherxUser> higherxUserOptional = higherxUserRepository.findById(higherxUserId);
        if (higherxUserOptional.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 유저정보 입니다.");
        }
        return higherxUserOptional.get();
    }

    @Override
    public Verify verifyAccount(String account) {
        Optional<HigherxUser> higherxUser = higherxUserRepository.findByAccount(account);
        return getVerify(higherxUser);
    }

    @Override
    public Verify verifyCrn(String crn) {
        Optional<HigherxUser> higherxUser = higherxUserRepository.findByCrn(crn);
        return getVerify(higherxUser);
    }

    @Override
    public Verify verifyNickname(String nickname) {
        Optional<HigherxUser> higherxUser = higherxUserRepository.findByNickname(nickname);
        return getVerify(higherxUser);
    }

    private void setEncodePassword(HigherxUser higherxUser) {
        String rawPassword = higherxUser.getPassword();
        String encodePassword = passwordEncoder.encode(rawPassword);
        higherxUser.encodePassword(encodePassword);
    }

    private static Verify getVerify(Optional<HigherxUser> higherxUser) {
        if (higherxUser.isEmpty()) {
            return Verify.nonExist();
        }
        return Verify.exist();
    }
}
