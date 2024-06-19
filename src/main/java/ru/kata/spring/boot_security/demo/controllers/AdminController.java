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

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index(Model model, @ModelAttribute("clearUser") User user) {
        model.addAttribute("usersList", service.getUsers());
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("clearUser", new User());
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
                             @ModelAttribute("users") @Valid User user, BindingResult result,
                            Model model) {
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        model.addAttribute("usersList", service.getUsers());
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("clearUser", new User());
        model.addAttribute("user", ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
        if(result.hasErrors()) {
            return "user/index_page";
        }
        service.updateUserById(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/create")
    public String createPerson(@ModelAttribute("clearUser") @Valid User user,
                               BindingResult result, @ModelAttribute("clearRole") Role role, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("usersList", service.getUsers());
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("user", ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
            return "user/index_page";
        }
        System.out.println(role);
        if(role.getShortRole().equals("ADMIN")) {
            user.setRoleList(Collections.singletonList(roleService.getRoleById(1)));
        } else {
            user.setRoleList(Collections.singletonList(roleService.getRoleById(2)));
        }

        service.addUser(user);
        return "redirect:/admin";
    }

}
