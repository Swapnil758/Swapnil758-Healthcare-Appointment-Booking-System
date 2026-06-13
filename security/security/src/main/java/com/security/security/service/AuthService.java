// AuthService.java
package com.security.security.service;

import com.security.security.dto.APIResponce;
import com.security.security.dto.UserDto;
import com.security.security.entity.User;
import com.security.security.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder en;

    public APIResponce<String> register(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            // ye galat tha:
            // pehle existsByUser(userDto.getUsername()) use ho raha tha
            // username check ke liye existsByUsername use hona chahiye

            APIResponce<String> responce = new APIResponce<>();
            responce.setMessage("Registration failed");
            responce.setStatus(409);
            // ye galat tha:
            // pehle 500 status diya ja raha tha
            // duplicate username/email server error nahi hota, isliye 409 better hai
            responce.setData("Username already exists");
            return responce;
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            APIResponce<String> responce = new APIResponce<>();
            responce.setMessage("Registration failed");
            responce.setStatus(409);
            // ye galat tha:
            // pehle duplicate case me 500 diya ja raha tha
            responce.setData("Email already exists");
            return responce;
        }

        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(en.encode(userDto.getPassword()));

        user.setRole("ROLE_USER");
        // ye galat tha:
        // pehle user.setRole("ROLE_ADMIN") tha
        // iska matlab har naya signup admin ban raha tha
        // normal signup me ROLE_USER hona chahiye

        userRepository.save(user);

        APIResponce<String> responce = new APIResponce<>();
        responce.setMessage("Registration successful");
        responce.setStatus(201);
        responce.setData("User is successfully registered");
        return responce;
    }
}