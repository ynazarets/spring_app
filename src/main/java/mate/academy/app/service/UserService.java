package mate.academy.app.service;

import mate.academy.app.dto.user.reg.UserRegistrationRequestDto;
import mate.academy.app.dto.user.reg.UserResponseDto;
import mate.academy.app.exception.RegistrationException;

public interface UserService {

    UserResponseDto registerUser(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

}
