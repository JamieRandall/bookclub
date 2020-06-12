package ru.club.transfer.club;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.adapters.PageAsList;
import ru.club.models.User;

@Data
@Builder
public class ClubMemberDto {
    private String firstName;
    private String lastName;
    private String login;

    public static PageAsList<ClubMemberDto> getDtosListFromPage(Page<User> users) {
        PageAsList<ClubMemberDto> dtoList = new PageAsList<>();

        for(User user : users) {
            dtoList.add(ClubMemberDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .lastName(user.getLogin())
                    .build());
        }
        dtoList.setTotalElements(users.getTotalElements());
        dtoList.setTotalPages(users.getTotalPages());

        return dtoList;
    }
}
