package com.project.streaming_dataservice.controllers;

//import com.project.streaming_dataservice.service.AuthClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.project.streaming_dataservice.model.User;
import com.project.streaming_dataservice.requests.RegistryUserRequest;
import com.project.streaming_dataservice.requests.UpdateUserRequest;
import com.project.streaming_dataservice.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
//    private final AuthClient authClient;, AuthClient authClient

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
//        this.authClient = authClient;
    }

    // Регистрация
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegistryUserRequest request) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());

        User createdUser = userService.registerUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Обновление профиля (нужно передать email, username или новый пароль)
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUserRequest request, Principal principal) {
        User existingUser = userService.findUserByUsername(principal.getName());

        if (request.getUsername() != null) {
            existingUser.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // Удаление аккаунта
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
        return ResponseEntity.ok("Account deleted successfully");
    }

//    @PutMapping("/update")
//    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUserRequest request,
//                                           @RequestHeader("Authorization") String token) {
//        if (authClient.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        User existingUser = userService.findUserByUsername(request.getUsername());
//
//        if (request.getPassword() != null) {
//            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//
//        User updatedUser = userService.updateUser(existingUser);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token,
//                                             @RequestParam String username) {
//        if (authClient.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//        }
//
//        userService.deleteUser(username);
//        return ResponseEntity.ok("Account deleted successfully");
//    }
}
