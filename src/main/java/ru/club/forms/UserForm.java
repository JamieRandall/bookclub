package ru.club.forms;

import lombok.Data;
import ru.club.models.Club;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserForm {
    @Size(min = 5)
    private String login;
    @Size(min = 5)
    private String password;
    private List<Club> clubs;
}
