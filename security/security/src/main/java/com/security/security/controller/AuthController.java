package com.security.security.controller;

import com.security.security.dto.APIResponce;
import com.security.security.dto.LoginDto;
import com.security.security.dto.UserDto;
import com.security.security.service.AuthService;
import com.security.security.service.JWTservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTservice jwTservice;

    @PostMapping("/signup")
    public ResponseEntity<APIResponce<String>> register(
            @RequestBody UserDto userDto) {
        APIResponce<String> responce = authService.register(userDto);
        return new ResponseEntity<>(responce,
                HttpStatusCode.valueOf(responce.getStatus()));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponce<String>> VerifyLogin(
            @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(), loginDto.getPassword());
        APIResponce<String> responce = new APIResponce<>();
        try {
            Authentication authenticate =
                    authenticationManager.authenticate(token);
            if (authenticate.isAuthenticated()) {
                String jwtToken = jwTservice.generationToken(
                        loginDto.getUsername(),
                        authenticate.getAuthorities()
                                .iterator().next().getAuthority());
                responce.setMessage("Login successful");
                responce.setData(jwtToken);
                responce.setStatus(200);
                return new ResponseEntity<>(responce,
                        HttpStatusCode.valueOf(responce.getStatus()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        responce.setMessage("failed");
        responce.setStatus(401);
        responce.setData("user login failed please try again");
        return new ResponseEntity<>(responce,
                HttpStatusCode.valueOf(responce.getStatus()));
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(
                userDetails.getUsername(), HttpStatus.OK);
    }
}