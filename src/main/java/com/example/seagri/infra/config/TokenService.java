package com.example.seagri.infra.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.seagri.infra.dto.UserDTO;
import com.example.seagri.infra.model.User;


@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        UserDTO userDTO = UserDTO.DTOFromEntity(user);
        Algorithm alg = Algorithm.HMAC256(secret);
        String token = JWT.create()
                          .withIssuer("seagri")
                          .withSubject(userDTO.userName())
                          .withClaim("id", userDTO.id())
                          .withClaim("fullName", userDTO.fullName())
                          .withClaim("active", userDTO.active())
                          .withExpiresAt(generateExpirationDate())
                          .sign(alg);
        return token;
    }

    public String validateToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        return JWT.require(alg)
                  .withIssuer("seagri")
                  .build()
                  .verify(token)
                  .getSubject();
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusDays(1)
                            .toInstant(ZoneOffset.of("-05:00"));
    }
    
}
