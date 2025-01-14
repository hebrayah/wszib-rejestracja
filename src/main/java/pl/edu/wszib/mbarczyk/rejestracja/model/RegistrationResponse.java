package pl.edu.wszib.mbarczyk.rejestracja.model;

import lombok.Data;

import java.util.Map;

@Data
public class RegistrationResponse {
    private Map<String, User> registeredUsers;
    private ApiError error;

    public RegistrationResponse(Map<String, User> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public RegistrationResponse() {
    }
}
