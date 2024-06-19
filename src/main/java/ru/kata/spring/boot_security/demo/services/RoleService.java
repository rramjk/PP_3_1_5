package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Role getRoleById(int id) {
        return repository.getRoleById(id);
    }
}
