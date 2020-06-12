package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.club.forms.LoginForm;
import ru.club.services.LoginService;
import ru.club.transfer.TokenDto;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "My App Service get test1 API", position = 1)
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginForm loginForm){
        return ResponseEntity.ok(loginService.login(loginForm));

    }
}
