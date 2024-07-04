package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDTO {
    private int id;

    @Size(min = 1, max = 100, message = "Имя должно быть больше 0 и меньше 100 символов!")
    @NotEmpty(message = "Поле не может быть пустым!")
    private String name;

    @Size(min = 1, max = 100, message = "Фамилия должна быть больше 0 и меньше 100 символов!")
    @NotEmpty(message = "Поле не может быть пустым!")
    private String lastName;

    @Min(value = 1, message = "Возраст может быть только больше 0!")
    private int age;

    @Email(message = "Не верно указан email!")
    @NotEmpty(message = "Поле не может быть пустым!")
    @Size(min = 1, max = 100, message = "Email должна быть больше 0 и меньше 100 символов!")
    private String email;

    @Size(min = 1, max = 100, message = "Неверный формат пароля!")
    @NotEmpty(message = "Поле не может быть пустым!")
    private String password;

    private List<Role> roleList;

    public UserDTO() {
    }

    public UserDTO(int id, String name, String lastName, int age, String email, String password, List<Role> roleList) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roleList = roleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
