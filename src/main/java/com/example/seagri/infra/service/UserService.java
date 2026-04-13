package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.dto.UserDTO;
import com.example.seagri.infra.model.User;
import com.example.seagri.infra.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authManager;

    public List<User> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Page<User> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public UserDTO getById(Long id) {
        return UserDTO.DTOFromEntity(repository.findById(id).orElse(null));
    }

    public User getByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    public User save(User object) {
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        if (object.getPassword() == null || object.getPassword().isEmpty()) {
            Long id = object.getId();
            @SuppressWarnings("null")
            User user = repository.findById(id).orElse(null);
            if (user != null) {
                object.setPassword(user.getPassword());
            }
        } else {
            object.setPassword(passEncoder.encode(object.getPassword()));
        }
        return repository.save(object);
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("unused")
    public Boolean compare(User user) {
      UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
      Authentication auth = authManager.authenticate(loginToken);

      return true;
    }

}
