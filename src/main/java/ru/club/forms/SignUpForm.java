package ru.club.forms;

import lombok.Data;

@Data
public class SignUpForm {
    private String firstName;
    private String lastName;
    private String login;
    private String password;
}
