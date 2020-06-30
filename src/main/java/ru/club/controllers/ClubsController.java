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
import ru.club.transfer.ClubDto;

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
    public ResponseEntity<ClubDto> getCertainClub(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = "token") String token) {
        return ResponseEntity.ok(clubsService.findClubById(id, token));
    }

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
            @RequestHeader (name = "token") String token,
            @RequestBody ClubForm clubForm) {

        return ResponseEntity.status(201).body(clubsService.createClub(clubForm, token));
    }

    @DeleteMapping("/{club-id}/delete")
    @ApiOperation(value = "Delete the club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> deleteClub(
            @RequestHeader (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {
        clubsService.deleteClub(clubId, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{club-id}/recover")
    @ApiOperation(value = "Recover deleted club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> recoverClub(
            @RequestHeader (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {
        clubsService.recoverClub(clubId, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{club-id}/join")
    @ApiOperation(value = "Leave a request to join certain club", authorizations = { @Authorization(value="token")})
    public ResponseEntity<Object> joinClub(
            @RequestHeader (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId,
            @RequestBody RequestForm requestForm) {

        int i = 0;
        clubsService.joinClub(clubId, token, requestForm);
        URI uri = URI.create("/clubs/" + clubId);

        return ResponseEntity.created(uri).body("Request's sent");
    }

    @DeleteMapping("/{club-id}/leave")
    @ApiOperation(value = "Leave the club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> leaveClub(
            @RequestHeader (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {
        clubsService.leaveClub(clubId, token);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{club-id}/change")
    @ApiOperation(value = "Change info of certain club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> changeClub(
            @RequestBody ClubForm clubForm,
            @RequestHeader (name = "token") String token,
            @PathVariable (value = "club-id") Long clubId) {

        clubsService.changeClub(clubForm, clubId, token);

        return ResponseEntity.ok("Club has been changed");
    }



}
