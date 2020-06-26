package ru.club.transfer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.decorators.PageMutable;
import ru.club.models.Club;
import ru.club.models.RequestStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubDto {
    private Long id;
    private String title;
    private String description;
    private String ownerLogin;
    private String userStatus;
    private List<UserDto> members;

    public static ClubDto getClubDto(Club club, String requestStatus) {
        return ClubDto.builder()
                .id(club.getId())
                .description(club.getDescription())
                .title(club.getTitle())
                .ownerLogin(club.getOwner().getLogin())
                .members(UserDto.getMembersListDto(club.getMembers()))
                .userStatus(requestStatus)
                .build();
    }

    public static List<ClubDto> getClubShortDtoList(List<Club> clubs) {
        List<ClubDto> dtos = new ArrayList<>();

        for (Club club : clubs) {
            dtos.add(ClubDto.builder()
            .id(club.getId())
            .title(club.getTitle())
            .build());
        }

        return dtos;
    }

    public static PageMutable<ClubDto> createMutableDtoPage(Page<Club> clubs) {
        List<ClubDto> innerList = new ArrayList<>();
        for (Club club : clubs) {
            innerList.add(ClubDto.builder()
                    .ownerLogin(club.getOwner().getLogin())
                    .title(club.getTitle())
                    .description(club.getDescription())
                    .members(UserDto.getMembersListDto(club.getMembers()))
                    .build());
        }

        return new PageMutable(innerList, clubs.getTotalPages(), clubs.getTotalElements(), clubs.getSort());
    }
}
