package ru.club.transfer.club;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.decorators.PageMutable;
import ru.club.models.Club;
import ru.club.models.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubDto {
    private String title;
    private String description;
    private String ownerLogin;
    private List<String> members;

    public static ClubDto fromClub(Club club) {
        List<String> memberLogins = new ArrayList<>();

        for (User member : club.getMembers())
            memberLogins.add(member.getLogin());

        return ClubDto.builder()
                .description(club.getDescription())
                .title(club.getTitle())
                .ownerLogin(club.getOwner().getLogin())
                .members(memberLogins)
                .build();
    }

    public static PageMutable<ClubDto> createMutableDtoPage(Page<Club> clubs) {
        List<ClubDto> innerList = new ArrayList<>();
        for (Club club : clubs) {
            List<String> memberLogins = new ArrayList<>();

            for (User member : club.getMembers()) {
                memberLogins.add(member.getLogin());
            }
            innerList.add(ClubDto.builder()
                    .ownerLogin(club.getOwner().getLogin())
                    .title(club.getTitle())
                    .description(club.getDescription())
                    .members(memberLogins)
                    .build());
        }

        return new PageMutable(innerList, clubs.getTotalPages(), clubs.getTotalElements(), clubs.getSort());
    }
}
