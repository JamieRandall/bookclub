package ru.club.transfer.user;

import lombok.Builder;
import lombok.Data;
import ru.club.models.Club;
import ru.club.models.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String login;
    private List<String> clubs;
    private List<String> ownClubs;

    public static UserProfileDto fromUser(User user) {
        List<String> clubsList = new ArrayList<>();
        List<String> ownClubsList = new ArrayList<>();
        for (Club club : user.getClubs()) {
            clubsList.add(club.getTitle());
        }
        for (Club club : user.getOwnedClubs()) {
            ownClubsList.add(club.getTitle());
        }

        return UserProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .login(user.getLogin())
                .clubs(clubsList)
                .ownClubs(ownClubsList)
                .build();
    }
}
