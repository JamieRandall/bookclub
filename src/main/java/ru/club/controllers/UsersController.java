package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.club.decorators.PageMutable;
import ru.club.exception.EntityNotFoundException;
import ru.club.forms.UserChangeForm;
import ru.club.services.UsersService;
import ru.club.transfer.UserDto;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    @ApiOperation(value = "Get all users combined by pages", authorizations = { @Authorization(value="token") })
    public PageMutable<UserDto> paginationGetUsers(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        return usersService.paginationFindAll(page, size);
    }

    @ApiOperation(value = "Get certain user info by id", authorizations = { @Authorization(value="token") })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getCertainUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(usersService.findUserById(id));
    }

    @ApiOperation(value = "Get homepage of user that's logged in", authorizations = { @Authorization(value="token") })
    @GetMapping("/mypage")
    public ResponseEntity<UserDto> getMyPage(@RequestHeader (name = "token") String token) {
        UserDto dto = usersService.getMyPage(token);

        return ResponseEntity.ok(dto);
    }

    @ApiOperation(value = "Get members of certain club by its id", authorizations = { @Authorization(value="token") })
    @GetMapping("/members-of/{club-id}")
    public PageMutable<UserDto> getMembersOfClub(
            @PathVariable("club-id") Long clubId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        return usersService.getMembersOfClub(clubId, page, size);
    }



    @ApiOperation(value = "Change information user by its id", authorizations = { @Authorization(value="token") })
    @PutMapping("/{user-id}/change")
    public ResponseEntity<Object> changeUser(
            @RequestBody UserChangeForm userChangeForm,
            @RequestHeader (name = "token") String token,
            @PathVariable(name = "user-id") Long userId) {

        usersService.changeUser(userChangeForm, userId, token);

        return ResponseEntity.ok("User has been changed");

    }

    @ApiOperation(value = "Upload user's photo", authorizations = { @Authorization(value="token") })
    @PostMapping("/{user-id}/uploadPhoto")
    public ResponseEntity<Object> uploadPhoto(
            @RequestParam(name = "photo") MultipartFile photo,
            @RequestHeader (name = "token") String token,
            @PathVariable(name = "user-id") Long userId) {

        usersService.uploadPhoto(photo, userId, token);

        return ResponseEntity.ok("Photo has been uploaded");

    }
}
