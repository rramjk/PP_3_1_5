package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;

    @Autowired
    public AdminController(UserService service) {
        this.service = service;
    }
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("usersList", service.getUsers());
        return "user/index";
    }

    @GetMapping("/show")
    public String showPerson(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        model.addAttribute("user", service.getUserById(id).get());
        return "user/show";
    }


    @PostMapping("/delete")
    public String deletePerson(@RequestParam(value = "id", required = false) Integer id) {
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        service.deleteUserById(id);
        return "redirect: /users";
    }

    @GetMapping("/edit")
    public String showCreatePage(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        model.addAttribute("user", service.getUserById(id).get());
        return "user/edit";
    }
    @PostMapping("/edit")
    public String editPerson(@RequestParam(value = "id", required = false) Integer id,
                             @ModelAttribute("user") @Valid User user, BindingResult result) {
        if (service.getUserById(id).isEmpty()) {
            return "user/exception";
        }
        if(result.hasErrors()) {
            return "user/edit";
        }
        service.updateUserById(id, user);
        return "redirect: /users/show?id="+id;
    }

    @GetMapping("/create")
    public String showCreatePage(@ModelAttribute("user") User user) {
        return "create";
    }

    @PostMapping("/create")
    public String createPerson(@ModelAttribute("user") @Valid User user,
                               BindingResult result) {
        if(result.hasErrors()) {
            return "user/create";
        }
        service.addUser(user);
        return "redirect: /users";
    }

}
