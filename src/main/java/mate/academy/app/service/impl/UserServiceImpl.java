package mate.academy.app.service.impl;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.user.reg.UserRegistrationRequestDto;
import mate.academy.app.dto.user.reg.UserResponseDto;
import mate.academy.app.exception.EntityNotFoundException;
import mate.academy.app.exception.RegistrationException;
import mate.academy.app.mapper.UserMapper;
import mate.academy.app.model.Role;
import mate.academy.app.model.User;
import mate.academy.app.model.enums.RoleName;
import mate.academy.app.repository.RoleRepository;
import mate.academy.app.repository.UserRepository;
import mate.academy.app.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not create a new User, "
                    + "this email already exist");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setShippingAddress(requestDto.getShippingAddress());
        Role userRole = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role by name: "
                        + RoleName.USER.name()));
        user.setRoles(Set.of(userRole));
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
