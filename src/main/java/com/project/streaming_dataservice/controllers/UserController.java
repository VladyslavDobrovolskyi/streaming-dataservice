package com.project.streaming_dataservice.controllers;

import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.requests.RegistryUserRequest;
import com.project.streaming_dataservice.requests.UpdateUserRequest;
import com.project.streaming_dataservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final String COOKIE_NAME = "userId";

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Регистрация без токена, с cookie
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegistryUserRequest request,
                                         HttpServletResponse response) {

        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(userId); // убедись, что поле id — строка
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User createdUser = userService.registerUser(user);

        Cookie cookie = new Cookie(COOKIE_NAME, userId);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 дней
        response.addCookie(cookie);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    
    @GetMapping("/info")
    public ResponseEntity<?> info(@CookieValue(value = COOKIE_NAME, required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid user ID");
        }
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(user.getUsername());
    }
    @PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody RegistryUserRequest request,
                               HttpServletResponse response) {

    // Найти пользователя по username
    User user;
    try {
        user = userService.findUserByUsername(request.getUsername());
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    // Проверить пароль
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    // Установить cookie с userId
    Cookie cookie = new Cookie(COOKIE_NAME, user.getId());
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 30); // 30 дней
    response.addCookie(cookie);

    return ResponseEntity.ok(user);
}


    // ✅ Обновление по userId из cookie
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateUserRequest request,
                                    HttpServletRequest servletRequest) {

        Optional<String> userId = getUserIdFromCookie(servletRequest);
        if (userId.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user cookie");

        User user = userService.findUserById(userId.get());

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));

        User updated = userService.updateUser(user);
        return ResponseEntity.ok(updated);
    }

    // ✅ Удаление по userId из cookie
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse) {

        Optional<String> userId = getUserIdFromCookie(servletRequest);
        if (userId.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user cookie");

        userService.deleteUserById(userId.get());

        // удаляем куку
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);

        return ResponseEntity.ok("User deleted");
    }

    private Optional<String> getUserIdFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
