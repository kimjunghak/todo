package com.higherx.api.controller.user;

import com.higherx.api.model.dto.user.HigherxUserFront;
import com.higherx.api.service.auth.JwtService;
import com.higherx.api.service.user.HigherxUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class HigherxUserController {

    private final JwtService jwtService;
    private final HigherxUserService higherxUserService;

    @PostMapping("")
    public void signup(@RequestBody HigherxUserFront.Signup signup) {
        higherxUserService.signup(signup);
    }

    @GetMapping("")
    public HigherxUserFront.UserInfo getUserInfo(HttpServletRequest request) {
        Long userId = jwtService.checkAuth(request);
        return higherxUserService.getInfo(userId);
    }

    @DeleteMapping("")
    public void signout(HttpServletRequest request) {
        Long userId = jwtService.checkAuth(request);
        higherxUserService.signout(userId);
    }
}
