package com.example.seagri.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.User;
import com.example.seagri.infra.service.UserService;

@Service
public class UserProfileService implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = service.getByUserName(username);
       return new UserProfile(user);
    }
    
}
