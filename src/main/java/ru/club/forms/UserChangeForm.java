package ru.club.forms;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserChangeForm {
    @Size(min = 5)
    private String login;
    @Size(min = 5)
    private String password;
    private String firstName;
    private String lastName;
    private String role;
}
