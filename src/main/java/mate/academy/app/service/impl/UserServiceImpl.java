package mate.academy.app.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.user.UserRegistrationRequestDto;
import mate.academy.app.dto.user.UserResponseDto;
import mate.academy.app.exception.RegistrationException;
import mate.academy.app.mapper.UserMapper;
import mate.academy.app.model.Role;
import mate.academy.app.model.User;
import mate.academy.app.model.enums.RoleName;
import mate.academy.app.repository.UserRepository;
import mate.academy.app.service.RoleService;
import mate.academy.app.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not create a new User, "
                    + "this email already exist");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        Role userRole = roleService.findRoleByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Can't find role by name: "
                        + RoleName.USER.name()));
        user.setRoles(Set.of(userRole));
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
