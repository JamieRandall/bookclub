package ru.club.transfer.club;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.adapters.PageAsList;
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

    public static PageAsList<ClubDto> getDtosListFromPage(Page<Club> clubs) {
        PageAsList<ClubDto> dtoList = new PageAsList<>();

        for(Club club : clubs) {
            List<String> memberLogins = new ArrayList<>();

            for (User member : club.getMembers()) {
                memberLogins.add(member.getLogin());
            }
            dtoList.add(ClubDto.builder()
                    .ownerLogin(club.getOwner().getLogin())
                    .title(club.getTitle())
                    .description(club.getDescription())
                    .members(memberLogins)
                    .build());
        }
        dtoList.setTotalElements(clubs.getTotalElements());
        dtoList.setTotalPages(clubs.getTotalPages());

        return dtoList;
    }
}
