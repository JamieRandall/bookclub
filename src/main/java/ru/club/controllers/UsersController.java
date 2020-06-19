package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.decorators.PageMutable;
import ru.club.forms.SignUpForm;
import ru.club.forms.UserChangeForm;
import ru.club.forms.UserForm;
import ru.club.models.User;
import ru.club.services.UsersService;
import ru.club.transfer.club.ClubDto;
import ru.club.transfer.user.UserPageDto;
import ru.club.transfer.user.UserProfileDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    @ApiOperation(value = "Get all users combined by pages", authorizations = { @Authorization(value="token") })
    public PageMutable<UserPageDto> paginationGetUsers(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        return usersService.paginationFindAll(page, size);
    }

    @ApiOperation(value = "Get certain user info by id", authorizations = { @Authorization(value="token") })
    @GetMapping("/{id}")
    public ResponseEntity<UserPageDto> getCertainUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(usersService.findUserById(id));
    }

    @ApiOperation(value = "Get homepage of user that's logged in", authorizations = { @Authorization(value="token") })
    @GetMapping("/mypage")
    public ResponseEntity<UserProfileDto> getMyPage(@RequestParam(name = "token") String token) {
        UserProfileDto dto = usersService.getMyPage(token);

        return ResponseEntity.ok(dto);
    }

    @ApiOperation(value = "Get members of certain club by its id", authorizations = { @Authorization(value="token") })
    @GetMapping("/members-of/{club-id}")
    public PageMutable<UserPageDto> getMembersOfClub(
            @PathVariable("club-id") Long clubId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        return usersService.getMembersOfClub(clubId, page, size);
    }


//    @ApiOperation(value = "Change information of my profile", authorizations = { @Authorization(value="token") })
//    @PutMapping("/mypage")
//    public ResponseEntity<Object> changeMyPage(@RequestBody SignUpForm signUpForm, @RequestParam(name = "token") String token) {
//
//        usersService.changeMyPage(signUpForm, token);
//
//        return ResponseEntity.ok("User changed");
//
//    }

    @ApiOperation(value = "Change information user by its id", authorizations = { @Authorization(value="token") })
    @PutMapping("/{user-id}/change")
    public ResponseEntity<Object> changeUser(
            @RequestBody UserChangeForm userChangeForm,
            @RequestParam(name = "token") String token,
            @PathVariable(name = "user-id") Long userId) {

        usersService.changeUser(userChangeForm, userId, token);

        return ResponseEntity.ok("User has been changed");

    }
}
