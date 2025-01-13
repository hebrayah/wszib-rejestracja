package pl.edu.wszib.mbarczyk.rejestracja.model;

public record User(String username,
                   String email,
                   String password
) {
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {
        this(null, null, null);
    }
}

