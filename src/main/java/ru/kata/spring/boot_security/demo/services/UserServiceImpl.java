package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleService roleService) {
        this.repository = repository;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        repository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUserById(int id, User user) {
        user.setId(id);
        repository.save(user);
    }

    public void setRoleAtUser(Role role, User owner) {
        if (role == null) {
            if (owner.getId() != 0) {
                role = getUserById(owner.getId()).get().getRoleList().getFirst();
            } else {
                owner.setRoleList(Collections.singletonList(roleService.getRoleById(2)));
                return;
            }
        }
        if(role.getShortRole().equals("ADMIN")) {
            owner.setRoleList(Collections.singletonList(roleService.getRoleById(1)));
        } else{
            owner.setRoleList(Collections.singletonList(roleService.getRoleById(2)));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(int id) {
        return repository.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return repository.findAll();
    }
}
