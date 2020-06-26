package ru.club.transfer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.decorators.PageMutable;
import ru.club.models.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String email;
    private List<ClubDto> clubs;

    public static UserDto getUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .login(user.getLogin())
                .clubs(ClubDto.getClubShortDtoList(user.getClubs()))
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> getMembersListDto(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserDto.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .build());
        }

        return dtos;
    }

    public static PageMutable<UserDto> createMutableDtoPage(Page<User> users) {
        List<UserDto> innerList = new ArrayList<>();
        for (User user : users) {
            innerList.add(UserDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .login(user.getLogin())
                    .clubs(ClubDto.getClubShortDtoList(user.getClubs()))
                    .build());
        }

        return new PageMutable(innerList, users.getTotalPages(), users.getTotalElements(), users.getSort());
    }
}
