package com.young.dev.youtubeclone.controller;

import com.young.dev.youtubeclone.service.UserRegistrationService;
import com.young.dev.youtubeclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;

    @GetMapping("/register")
    @ResponseStatus(OK)
    public String register(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        userRegistrationService.registerUser(jwt.getTokenValue());
        return "User Registration successful";
    }

    @PostMapping("/subscribe/{userId}")
    @ResponseStatus(OK)
    public boolean subscribeUser(@PathVariable String userId) {
        userService.subscribeUser(userId);
        return true;
    }

    @PostMapping("/unSubscribe/{userId}")
    @ResponseStatus(OK)
    public boolean unSubscribeUser(@PathVariable String userId) {
        userService.unSubscribeUser(userId);
        return true;
    }

    @GetMapping("/{userId}/history")
    @ResponseStatus(OK)
    public Set<String> userHistory(@PathVariable String userId) {
        return userService.userHistory(userId);
    }
}
