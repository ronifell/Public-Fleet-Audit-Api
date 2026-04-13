package com.example.seagri.infra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.dto.UserDTO;
import com.example.seagri.infra.model.User;
import com.example.seagri.infra.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService service;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll() {
        List<User> allUsers = service.getAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/paged/")
    public ResponseEntity<Page<User>> getPage(Pageable page) {
        Page<User> pagedUsers = service.getPage(page);
        return new ResponseEntity<>(pagedUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        UserDTO instance = service.getById(id);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<User> insert(@RequestBody User object) {
        User instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<User> update(@RequestBody User object) {
        User instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/compare")
    public ResponseEntity<Boolean> comparePasswords(@RequestBody User user) {
        // Boolean result = service.comparePasswords(user);
        boolean resp = false;
        try{
            resp = service.compare(user);
        } catch(Exception e){
            resp = false;
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
