package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addUser(User user) {
        repository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        repository.deleteById(id);
    }

    @Override
    public void updateUserById(int id, User user) {
        user.setId(id);
        addUser(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return repository.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }
}
