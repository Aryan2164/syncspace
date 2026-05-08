package com.syncspace.backend.service;

import com.syncspace.backend.entity.User;
import com.syncspace.backend.exception.EmailAlreadyExistsException;
import com.syncspace.backend.repository.UserRepository;
import com.syncspace.backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder encoder){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    // Register
    public void registerUser(User user) {

        boolean exists = userRepository.existsByEmail(user.getEmail());

        if (exists) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
         String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);


    }

    //  Login
    public String loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Invalid email");
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}