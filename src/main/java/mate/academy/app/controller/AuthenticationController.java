package mate.academy.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.user.UserRegistrationRequestDto;
import mate.academy.app.dto.user.UserResponseDto;
import mate.academy.app.exception.RegistrationException;
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

    @PostMapping("/registration")
    UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.registerUser(requestDto);
    }
}
