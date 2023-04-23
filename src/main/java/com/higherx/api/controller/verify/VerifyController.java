package com.higherx.api.controller.verify;

import com.higherx.api.model.dto.user.Verify;
import com.higherx.api.service.user.HigherxUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class VerifyController {

    private final HigherxUserService higherxUserService;

    @GetMapping("/account")
    public Verify verifyAccount(@RequestParam String account) {
        return higherxUserService.verifyAccount(account);
    }

    @GetMapping("/crn")
    public Verify verifyCrn(@RequestParam String crn) {
        return higherxUserService.verifyCrn(crn);
    }

    @GetMapping("/nickname")
    public Verify verifyNickname(@RequestParam String nickname) {
        return higherxUserService.verifyNickname(nickname);
    }
}
