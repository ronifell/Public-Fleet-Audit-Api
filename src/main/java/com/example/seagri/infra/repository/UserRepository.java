package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUserName(String userName);
}
