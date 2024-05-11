package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Role")
public class Role implements GrantedAuthority {

    @Id
    private int id;
    @Column(name = "role")
    private String role;

    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                '}';
    }
}
