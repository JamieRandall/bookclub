package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.decorators.PageMutable;
import ru.club.forms.ClubForm;
import ru.club.forms.RequestForm;
import ru.club.services.ClubsService;
import ru.club.transfer.RequestDto;
import ru.club.transfer.club.ClubDto;

import java.net.URI;

@RestController
@RequestMapping("/clubs")
public class ClubsController {
    @Autowired
    private ClubsService clubsService;

    @GetMapping
    @ApiOperation(value = "Show clubs list", authorizations = { @Authorization(value="token") })
    public ResponseEntity<PageMutable<ClubDto>> paginationGetClubs(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        return ResponseEntity.ok(clubsService.paginationFindAll(page,size));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Show club by title", authorizations = { @Authorization(value="token") })
    public ResponseEntity<ClubDto> getCertainClub(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(clubsService.findClubById(id));
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

    @GetMapping("/owned-by/{user-id}")
    @ApiOperation(value = "Show clubs owned by certain user", authorizations = { @Authorization(value="token") })
    public ResponseEntity<PageMutable<ClubDto>> getClubsOwnedByUser(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @PathVariable(value = "user-id") Long userId) {


        return ResponseEntity.ok(clubsService.getClubsOwnedByUser(userId, page, size));
    }

    @GetMapping("/has-user/{user-id}")
    @ApiOperation(value = "Show clubs that has certain user as a member", authorizations = { @Authorization(value="token") })
    public ResponseEntity<PageMutable<ClubDto>> getClubsUserParticipate(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @PathVariable(value = "user-id") Long userId) {


        return ResponseEntity.ok(clubsService.getClubsUserParticipate(userId, page, size));
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create new club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> createClub(
            @RequestParam (name = "token") String token,
            @RequestBody ClubForm clubForm) {
        Long newClubId = clubsService.createClub(clubForm, token);
        URI uri = URI.create("/clubs/" + newClubId);

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{club-id}/delete")
    @ApiOperation(value = "Delete the club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> deleteClub(
            @RequestParam (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {
        clubsService.deleteClub(clubId, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{club-id}/recover")
    @ApiOperation(value = "Recover deleted club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> recoverClub(
            @RequestParam (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {
        clubsService.recoverClub(clubId, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{club-id}/join")
    @ApiOperation(value = "Leave a request to join certain club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> joinClub(
            @RequestParam (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId,
            @RequestBody RequestForm requestForm) {
        clubsService.joinClub(clubId, token, requestForm);
        URI uri = URI.create("/clubs/" + clubId);

        return ResponseEntity.created(uri).body("Request's sent");
    }

    @DeleteMapping("/{club-id}/leave")
    @ApiOperation(value = "Leave the club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> leaveClub(
            @RequestParam (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {
        clubsService.leaveClub(clubId, token);

        return ResponseEntity.ok().build();
    }



}
