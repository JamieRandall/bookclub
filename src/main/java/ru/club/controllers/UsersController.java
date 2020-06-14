package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.forms.UserForm;
import ru.club.models.User;
import ru.club.services.UsersService;
import ru.club.transfer.user.UserPageDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="token") })
    public Page<User> paginationGetUsers(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {
        if (page == null || size == null) {
            page = 0;
            size = 5;
        }
        return usersService.paginationFindAll(page, size);
    }

    @GetMapping("/findall")
    @ApiOperation(value = "", authorizations = { @Authorization(value="token") })
    public List<User> getUsers() {

        return usersService.findAll();
    }

    @PostMapping("/add")
    @ApiOperation(value = "", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> signUp(@RequestBody UserForm userForm) {
        usersService.signUp(userForm);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get certain user info by login", authorizations = { @Authorization(value="token") })
    @GetMapping("/{login}")
    public ResponseEntity<UserPageDto> getUserPage(@PathVariable(name = "login") String login) {
        Optional<UserPageDto> userCandidate = usersService.findUserByLogin(login);
        ResponseEntity entity;
        if (userCandidate.isPresent()) {
            entity = ResponseEntity.ok(userCandidate.get());
        } else {
            entity = ResponseEntity.status(404).body("Not found");
        }
        return entity;
    }



//    @PutMapping("/change")
//    public ResponseEntity<Object> changeUser(@RequestBody UserForm userForm) {
//
//    }
}
