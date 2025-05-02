package com.community.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.community.model.User;
import com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.dto.LoginRequest;
import com.community.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        boolean isDuplicate = userService.existsByEmail(user.getEmail());
        if (isDuplicate){
            return ResponseEntity.status(409).body("Email is already in use");
        } else{
            userService.saveUser(user);
            return ResponseEntity.ok("Email is available");
        }
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

        String token = jwtUtil.generateToken(user.getId(),user.getUsername(),user.getEmail(), user.getRole().name());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promote")
    public ResponseEntity<?> promoteToAdmin(@RequestHeader("Authorization") String token){
        String email = jwtUtil.extractEmail(token.substring(7));
        String result = userService.promoteToAdmin(email);
        return ResponseEntity.ok(result);
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

