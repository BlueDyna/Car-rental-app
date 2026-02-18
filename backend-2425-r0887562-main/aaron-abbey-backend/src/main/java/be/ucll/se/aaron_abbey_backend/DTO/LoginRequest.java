package be.ucll.se.aaron_abbey_backend.DTO;

import be.ucll.se.aaron_abbey_backend.model.User;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static User toUser(LoginRequest dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }
}
