package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.adapters.PageAsList;
import ru.club.models.Club;
import ru.club.models.User;
import ru.club.services.ClubsService;
import ru.club.transfer.club.ClubDto;
import ru.club.transfer.club.ClubMemberDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clubs")
public class ClubsController {
    @Autowired
    private ClubsService clubsService;

    @GetMapping
    @ApiOperation(value = "Show clubs list", authorizations = { @Authorization(value="token") })
    public PageAsList<ClubDto> showClubs(@RequestParam Integer page, @RequestParam Integer size) {
        return clubsService.paginationFindAll(page,size);
    }

    @GetMapping("/{title}")
    @ApiOperation(value = "Show club by title", authorizations = { @Authorization(value="token") })
    public ResponseEntity<ClubDto> showCertainClub(@PathVariable(value = "title") String title) {
        Optional<Club> clubCandidate = clubsService.findClubByTitle(title);
        ResponseEntity entity;
        if (clubCandidate.isPresent()) {
            entity = ResponseEntity.ok(clubCandidate.get());
        } else {
            entity = ResponseEntity.status(404).body("Not found");
        }

        return entity;
    }

//    @GetMapping("/{title}/members")
//    @ApiOperation(value = "Show members of certain club")
//    public PageAsList<ClubMemberDto> showCertainClubMembers(
//            @RequestParam Integer page,
//            @RequestParam Integer size,
//            @PathVariable(value = "title") String title) {
//
//
//        return new PageAsList<>();
//    }

    @GetMapping("/{title}/members")
    @ApiOperation(value = "Show members of certain club", authorizations = { @Authorization(value="token") })
    public List<User> showCertainClubMembers(
            @PathVariable(value = "title") String title) {


        return clubsService.getUsersByClubTitle(title);
    }
}
