package com.security.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JWTservice {
    private static final String SECRET_KEY="my-super-secret-key";
    private static final long EXPIRATION=86400000;
    public String generationToken(String username,String role){
        return JWT.create()
                .withSubject(username)
                .withClaim("role",role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET_KEY));

    }
    public static String validateTokenAndRetrieveSubject(String token){
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();

    }

}
