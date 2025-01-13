package pl.edu.wszib.mbarczyk.rejestracja.model;

import lombok.Data;

import java.util.Map;

@Data
public class RegistrationResponse {
    private final Map<String, User> registeredUsers;
}
