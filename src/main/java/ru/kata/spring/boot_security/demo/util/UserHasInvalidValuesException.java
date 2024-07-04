package ru.kata.spring.boot_security.demo.util;

public class UserHasInvalidValuesException extends Exception{
    public UserHasInvalidValuesException(String message) {
        super(message);
    }
}
