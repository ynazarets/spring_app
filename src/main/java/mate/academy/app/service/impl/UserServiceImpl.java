package mate.academy.app.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.user.UserRegistrationRequestDto;
import mate.academy.app.dto.user.UserResponseDto;
import mate.academy.app.exception.RegistrationException;
import mate.academy.app.mapper.UserMapper;
import mate.academy.app.model.User;
import mate.academy.app.repository.UserRepository;
import mate.academy.app.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

        @Override
        public UserResponseDto registerUser(UserRegistrationRequestDto requestDto)
                throws RegistrationException {
            if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
                throw new RegistrationException("Can not create a new User, "
                        + "this email already exist");
            }
            User user = new User();
            user.setEmail(requestDto.getEmail());
            user.setPassword(requestDto.getPassword());
            user.setFirstName(requestDto.getFirstName());
            user.setLastName(requestDto.getLastName());
            user.setShippingAddress(requestDto.getShippingAddress());
            return userMapper.toUserResponseDto(userRepository.save(user));
        }
    }
