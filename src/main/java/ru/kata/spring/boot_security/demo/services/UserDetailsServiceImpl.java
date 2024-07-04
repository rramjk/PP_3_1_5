package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private UserDaoImpl userDao;


    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, UserDaoImpl userDao) {
        this.userRepository = userRepository;
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDao.getUserByEmail(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Имя пользователя не найдено!");
        }
        User user = optionalUser.get();
        return new ru.kata.spring.boot_security.demo.security.UserDetails(user);
    }
}
