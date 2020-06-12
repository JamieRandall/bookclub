package ru.club.transfer.user;

import lombok.Builder;
import lombok.Data;
import ru.club.models.Club;
import ru.club.models.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserPageDto {
    private String firstName;
    private String lastName;
    private String login;
    private List<String> clubs;

    public static UserPageDto fromUser(User user) {
        List<String> clubsList = new ArrayList<>();
        for (Club club : user.getClubs()) {
            clubsList.add(club.getTitle());
        }

        return UserPageDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .login(user.getLogin())
                .clubs(clubsList)
                .build();
    }
}
