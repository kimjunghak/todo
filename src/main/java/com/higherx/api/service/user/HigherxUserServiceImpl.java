package com.higherx.api.service.user;

import com.higherx.api.config.UnAuthorizedException;
import com.higherx.api.encoder.PasswordEncoder;
import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.model.dto.user.HigherxUserMapper;
import com.higherx.api.model.dto.user.Verify;
import com.higherx.api.model.entity.user.HigherxUser;
import com.higherx.api.repo.user.HigherxUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class HigherxUserServiceImpl implements HigherxUserService{

    private final HigherxUserRepository higherxUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final HigherxUserMapper higherxUserMapper;

    @Override
    public void signup(HigherxUserFront.Signup signup) {
        HigherxUser higherxUser = higherxUserMapper.toEntity(signup);
        setEncodePassword(higherxUser);
        higherxUserRepository.save(higherxUser);
    }

    @Override
    public Long login(HigherxUserFront.Login login) {
        Optional<HigherxUser> higherxUserOptional = higherxUserRepository.findByAccount(login.getAccount());
        if (higherxUserOptional.isEmpty()) {
            throw new UnAuthorizedException("아이디 혹은 암호를 확인해주세요.");
        }
        HigherxUser higherxUser = higherxUserOptional.get();
        //암호 확인
        if (!passwordEncoder.matches(login.getPassword(), higherxUser.getPassword())) {
            throw new UnAuthorizedException("아이디 혹은 암호를 확인해주세요.");
        }
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
        String phone = higherxUser.getPhone();
        String crn = higherxUser.getCrn();
        higherxUser.signout(phoneMasking(phone), crnMasking(crn)); //마스킹 처리
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
        String replaceCrn = crn.replaceAll("-", "");
        int[] crnArray = Arrays.stream(replaceCrn.split("")).mapToInt(Integer::parseInt).toArray();
        if (!checkCrn(crnArray)) {
            return Verify.unavailable();
        }
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
            return Verify.available();
        }
        return Verify.unavailable();
    }

    private static boolean checkCrn(int[] crnCharArray) {
        final String authKey = "137137135";
        int[] authKeyArr = Arrays.stream(authKey.split("")).mapToInt(Integer::parseInt).toArray();
        // 1
        if (crnCharArray.length != 10) {
            throw new RuntimeException("사업자 번호는 - 제외 10자리어야 합니다.");
        }
        int sum = 0;
        // 2
        for (int i = 0; i < crnCharArray.length - 1; i++) {
            sum += crnCharArray[i] * authKeyArr[i];
        }
        // 3, 4
        sum += crnCharArray[crnCharArray.length - 2] * authKeyArr[authKeyArr.length - 1] / 10;
        // 5, 6
        int last = 10 - sum % 10;
        // 7
        return last == crnCharArray[crnCharArray.length - 1];
    }

    private String phoneMasking(String phone) {
        String regex = "(\\d{3})-?(\\d{4})-?(\\d{4})";
        return masking(regex, phone, 2);
    }

    private String crnMasking(String crn) {
        String regex = "(\\d{3})-?(\\d{2})-?(\\d{5})";
        return masking(regex, crn, 3);
    }

    private static String masking(String regex, String str, int target) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str);
        if (matcher.find()) {
            String last = matcher.group(target);
            String masking = last.replaceAll("\\d", "*");
            return str.replace(last, masking);
        }
        return Strings.EMPTY;
    }
}
