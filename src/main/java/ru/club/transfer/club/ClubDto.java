package ru.club.transfer.club;

import lombok.Builder;
import lombok.Data;
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

    public static PageMutable<ClubDto> changeInnerListToDto(PageMutable<Club> clubs) {
        List<ClubDto> innerList = new ArrayList<>();
        for (Club club : clubs.getInnerList()) {
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

        return new PageMutable<>(innerList, clubs.getTotalPages(), clubs.getTotalElements());
    }
}
