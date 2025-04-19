package com.blogdemo.controller;

import java.util.List;

import com.blogdemo.model.User;
import com.blogdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogdemo.dto.LoginRequest;
import com.blogdemo.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        boolean matches = new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword());
        if (!matches) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok().body("JWT Token: " + token);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email){
        boolean isDuplicate = userService.existsByEmail(email);
        if (isDuplicate){
            return ResponseEntity.status(409).body("Email is already in use");
        } else{
            return ResponseEntity.ok("Email is available");
        }
    }

}

