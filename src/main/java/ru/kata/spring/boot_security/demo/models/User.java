package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.springframework.stereotype.Component;


import java.util.List;

@Entity
@Component
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min=1, max=100, message = "Имя должно быть больше 0 и меньше 100 символов!")
    @NotEmpty(message = "Поле не может быть пустым!")
    private String name;

    @Column(name = "last_name")
    @Size(min=1, max=100, message = "Фамилия должна быть больше 0 и меньше 100 символов!")
    @NotEmpty(message = "Поле не может быть пустым!")
    private String lastName;

    @Column(name = "age")
    @Min(value = 1, message = "Возраст может быть только больше 0!")
    private int age;

    @Column(name = "email")
    @Email(message = "Не верно указан email!")
    @NotEmpty(message = "Поле не может быть пустым!")
    @Size(min=1, max=100, message = "Email должна быть больше 0 и меньше 100 символов!")
    private String email;

    @Column(name = "password")
    @Size(min=1, max=100, message = "Неверный формат пароля!")
    @NotEmpty(message = "Поле не может быть пустым!")
    private String password;

    @ManyToMany
    @JoinTable(name = "User_Role",
    joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList;

    public User() {

    }
    public User(String name, String lastName, int age, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}
