package ru.club.transfer.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.decorators.PageMutable;
import ru.club.models.Club;
import ru.club.models.User;
import ru.club.transfer.club.ClubDto;

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

    public static PageMutable<UserPageDto> createMutableDtoPage(Page<User> users) {
        List<UserPageDto> innerList = new ArrayList<>();
        for (User user : users) {
            List<String> clubTitles = new ArrayList<>();

            for (Club club : user.getClubs()) {
                clubTitles.add(club.getTitle());
            }

            innerList.add(UserPageDto.builder()
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .login(user.getLogin())
            .clubs(clubTitles)
            .build());
        }

        return new PageMutable(innerList, users.getTotalPages(), users.getTotalElements(), users.getSort());
    }
}
