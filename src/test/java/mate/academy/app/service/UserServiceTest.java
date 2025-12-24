package mate.academy.app.service;

import mate.academy.app.dto.user.reg.UserRegistrationRequestDto;
import mate.academy.app.dto.user.reg.UserResponseDto;
import mate.academy.app.exception.RegistrationException;
import mate.academy.app.mapper.UserMapper;
import mate.academy.app.model.Role;
import mate.academy.app.model.ShoppingCart;
import mate.academy.app.model.User;
import mate.academy.app.model.enums.RoleName;
import mate.academy.app.repository.RoleRepository;
import mate.academy.app.repository.UserRepository;
import mate.academy.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder  passwordEncoder;
    @Mock
    ShoppingCartService shoppingCartService;

    @DisplayName("""
            
            """)
    @Test
    public void registerUser_ValidRequest_ShouldReturnCreatedUser() throws RegistrationException {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setFirstName("FirstName");
        requestDto.setLastName("LastName");
        requestDto.setPassword("password");
        requestDto.setRepeatPassword("password");
        requestDto.setEmail("email");
        requestDto.setShippingAddress("address");

        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setEmail("email");
        user.setShippingAddress("address");

        Role userRole = new Role();
        userRole.setRoleName(RoleName.USER);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setFirstName(requestDto.getFirstName());
        userResponseDto.setLastName(requestDto.getLastName());
        userResponseDto.setEmail(requestDto.getEmail());
        userResponseDto.setShippingAddress(requestDto.getShippingAddress());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(userResponseDto.getId());

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);
        when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("password");

        UserResponseDto actualDto = userServiceImpl.registerUser(requestDto);

        assertNotNull(actualDto);
        assertNotNull(actualDto.getId());
        assertThat(actualDto)
                .usingRecursiveComparison()
                .isEqualTo(userResponseDto);

        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserResponseDto(user);
        verify(userMapper, times(1)).toModel(requestDto);
        verify(roleRepository, times(1)).findByRoleName(RoleName.USER);
        verify(passwordEncoder, times(1)).encode(requestDto.getPassword());
        verify(shoppingCartService).createShoppingCart(user);
    }

    @DisplayName("""
            Register user: successfully create user, encode password and initialize shopping cart
            """)
    @Test
    public void createUser_InvalidRequest_ShouldReturnRegistrationException() throws RegistrationException {

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("email");

        String expectedMessage = "Can not create a new User, this email already exist";

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(RegistrationException.class, ()
        -> userServiceImpl.registerUser(requestDto));
        assertEquals(expectedMessage, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(requestDto.getEmail());
        verifyNoInteractions(passwordEncoder, shoppingCartService, roleRepository);
    }
}
