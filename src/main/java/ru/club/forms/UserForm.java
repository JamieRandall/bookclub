package ru.club.forms;

import lombok.Data;
import ru.club.models.Club;

import java.util.List;

@Data
public class UserForm {
    private String login;
    private String password;
    private List<Club> clubs;
}
