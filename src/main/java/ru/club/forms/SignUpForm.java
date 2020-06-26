package ru.club.forms;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignUpForm {
    public static String eMailRegex = "[a-z]+(.?[-a-z0-9_]+)*@[a-z]+[a-z0-9]*(.[a-z]+)+";
    private String firstName;
    private String lastName;
    @Pattern(
            regexp = "[a-z]+(.?[-a-z0-9_]+)*@[a-z]+[a-z0-9]*(.[a-z]+)+",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
    @Size(min = 5)
    private String login;
    @Size(min = 5)
    private String password;
}
