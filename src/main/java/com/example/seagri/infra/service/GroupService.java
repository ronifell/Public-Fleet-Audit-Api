package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Group;
import com.example.seagri.infra.repository.GroupRepository;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository repository;

    public List<Group> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Page<Group> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public Group getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Group save(Group objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
