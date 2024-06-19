package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetails;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService service, RoleService roleService, UserValidator userValidator) {
        this.service = service;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }


    @GetMapping()
    public String index(Model model, @ModelAttribute("clearUser") User user, BindingResult result) {
        model.addAttribute("usersList", service.getUsers());
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("clearUser", new User());
        model.addAttribute("editedUser", new User());
        model.addAttribute("clearRole", new Role());
        model.addAttribute("user", ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
        return "user/index_page";
    }


    @PostMapping("/delete")
    public String deletePerson(@RequestParam(value = "id", required = false) Integer id) {
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        service.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editPerson(@RequestParam(value = "id", required = false) Integer id,
                             @ModelAttribute("editedUser") @Valid User user, BindingResult result,
                            Model model, @ModelAttribute("clearRole") Role role) {
        userValidator.validate(user, result);
        model.addAttribute("usersList", service.getUsers());
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("clearUser", new User());
        model.addAttribute("user", ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        if(result.hasErrors()) {
            return "user/index_page";
        }
        service.setRoleAtUser(role, user);
        service.updateUserById(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/create")
    public String createPerson(@ModelAttribute("clearUser") @Valid User user,
                               BindingResult result, @ModelAttribute("clearRole") Role role, Model model) {
        userValidator.validate(user, result);
        if(result.hasErrors()) {
            model.addAttribute("usersList", service.getUsers());
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("user", ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
            return "user/index_page";
        }
        service.setRoleAtUser(role, user);
        service.addUser(user);
        return "redirect:/admin";
    }

}
