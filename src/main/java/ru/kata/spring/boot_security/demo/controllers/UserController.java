package ru.kata.spring.boot_security.demo.controllers;

import javax.validation.Valid;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetails;
import ru.kata.spring.boot_security.demo.services.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String showPerson(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", service.getUserById(((UserDetails) authentication.getPrincipal()).getUser().getId()).get());
        return "user/users_show";
    }

}
