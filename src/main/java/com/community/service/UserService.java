package com.community.service;

import java.util.List;

import com.community.model.Role;
import com.community.model.User;
import com.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveUser(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public String promoteToAdmin(String email){
        if (userRepository.existsByRole(Role.ADMIN)){
            throw new IllegalStateException("이미 관리자가 존재합니다.");
        }
        User user = userRepository.findOptionalByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.setRole(Role.ADMIN);
        return "관리자 권한이 부여되었습니다.";
    }
}
