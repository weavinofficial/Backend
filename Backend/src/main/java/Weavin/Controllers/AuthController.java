package Weavin.Controllers;

import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Weavin.Models.SignUpUserHolder;
import Weavin.Services.AuthService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@Validated @RequestBody SignUpUserHolder user) {
        authService.signup(user);
        return "signed up";
    }

    @RequestMapping("/recoverPassword")
    public String recoverPassword() {
        authService.recoverPassword();
        return "password recovery email sent";
    }

    @RequestMapping("/delete")
    public String deleteUser() {
        authService.deleteUser();
        return "deleteUser";
    }
}
