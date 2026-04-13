package com.example.seagri.infra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.config.UserProfile;
import com.example.seagri.infra.dto.TokenResponseDTO;
import com.example.seagri.infra.model.User;
import com.example.seagri.infra.service.UserService;

import com.example.seagri.infra.config.TokenService;


@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody User user) {

        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication auth = authManager.authenticate(loginToken);
        UserProfile principal = (UserProfile) auth.getPrincipal();
        User authUser = userService.getByUserName(principal.getUsername());

        String token = tokenService.generateToken(authUser);
        TokenResponseDTO tokenResponse = new TokenResponseDTO(token);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User instance = userService.save(user);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);

    }
    
}
