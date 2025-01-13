package pl.edu.wszib.mbarczyk.rejestracja.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class RegistrationRequest {
    private final List<User> requestedUsers;
}
