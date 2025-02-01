package pl.edu.wszib.mbarczyk.rejestracja.model;

import java.util.List;

public record RegistrationRequest(List<User> requestedUsers) {
}
