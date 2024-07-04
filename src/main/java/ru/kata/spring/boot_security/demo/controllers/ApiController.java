package ru.kata.spring.boot_security.demo.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetails;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserHasInvalidValuesException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class ApiController {
    private ModelMapper modelMapper;
    private UserService service;
    private RoleService roleService;
    private UserValidator userValidator;

    @Autowired
    public ApiController(ModelMapper modelMapper, UserService service, RoleService roleService, UserValidator userValidator) {
        this.modelMapper = modelMapper;
        this.service = service;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping("")
    public List<UserDTO> getUsers() {
        return service.getUsers().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
        return convertToUserDTO(service.getUserById(id));
    }

    @GetMapping("/current")
    public User getCurrentUser(){
        User user= ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return  user;
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deletePerson(@PathVariable("id") Integer id) {
        service.deleteUserById(id);
        return new HttpEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public HttpEntity<HttpStatus> editPerson(@PathVariable("id") Integer id,
                             @RequestBody @Valid UserDTO userDTO, BindingResult result
    ) throws UserHasInvalidValuesException {
        User user = convertToUser(userDTO);

        userValidator.validate(user, result);
        if(result.hasErrors()) {
            throw new UserHasInvalidValuesException("Некорректные данные пользователя!");
        }
        service.setRoleAtUser(user.getRoleList().getFirst(), user);
        service.updateUserById(id, user);
        return new HttpEntity<>(HttpStatus.OK);
    }
    @PostMapping("")
    public HttpEntity<HttpStatus> createPerson(@RequestBody @Valid UserDTO userDTO,
                               BindingResult result) throws UserHasInvalidValuesException {
        User user = convertToUser(userDTO);
        userValidator.validate(user, result);
        if(result.hasErrors()) {
            throw new UserHasInvalidValuesException("Некорректные данные пользователя!");
        }
        service.setRoleAtUser(user.getRoleList().getFirst(), user);
        service.addUser(user);
        return new HttpEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> userNotFoundExc(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> userHasIncorrectValuesExc(UserHasInvalidValuesException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
