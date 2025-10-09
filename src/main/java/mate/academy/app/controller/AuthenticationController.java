package mate.academy.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.user.auth.UserLoginRequestDto;
import mate.academy.app.dto.user.auth.UserLoginResponseDto;
import mate.academy.app.dto.user.reg.UserRegistrationRequestDto;
import mate.academy.app.dto.user.reg.UserResponseDto;
import mate.academy.app.exception.RegistrationException;
import mate.academy.app.security.auth.AuthenticationService;
import mate.academy.app.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    @Operation(summary = "Registration a new User",
            description = "Registration a new user and checking input data")
    UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.registerUser(requestDto);
    }
}
