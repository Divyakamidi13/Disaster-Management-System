package com.disaster.disaster_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.disaster.disaster_management.model.User;
import com.disaster.disaster_management.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        User existing = userRepository.findByEmail(user.getEmail());

        if(existing != null){
            throw new RuntimeException("Email already registered");
        }

        return userRepository.save(user);
    }

    // LOGIN
    @PostMapping("/login")
    public User login(@RequestBody User user){

        User existing = userRepository.findByEmail(user.getEmail());

        if(existing == null){
            throw new RuntimeException("User not found");
        }

        if(!existing.getPassword().equals(user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return existing;
    }

}