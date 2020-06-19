package ru.club.forms;

import lombok.Data;

@Data
public class UserChangeForm {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
}
