package ru.club.controllers;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.adapters.PageAsList;
import ru.club.services.HomeService;
import ru.club.transfer.club.ClubDto;
import ru.club.transfer.user.UserProfileDto;

import java.util.Optional;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping
    @ApiOperation(value = "Get profile homepage", authorizations = { @Authorization(value="token") })
    public ResponseEntity<UserProfileDto> getMyProfile(@RequestParam(name = "token") String token) {
        Optional<UserProfileDto> dto = homeService.getProfileInfo(token);
        ResponseEntity entity;

        if (dto.isPresent()) {
            entity = ResponseEntity.ok(dto.get());
        } else {
            entity = ResponseEntity.status(404).body("Not found");
        }

        return entity;
    }

    @GetMapping("/myclubs")
    @ApiOperation(value = "Get clubs that i own", authorizations = { @Authorization(value="token") })
    public PageAsList<ClubDto> getMyClubs(
            @RequestParam(name = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "token") String token) {


        return new PageAsList<>();
    }

    @PostMapping("/myclubs/{title}/create")
    @ApiOperation(value = "Create new club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> createNewClub(
            @RequestParam(value = "token") String token,
            @PathVariable(value = "title") String title) {


        return ResponseEntity.ok().build();
    }
}
