package com.vvs.demo.project.security.impl;

import com.vvs.demo.project.dto.mapper.UserMapper;
import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.exception.AuthenticationException;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationServiceImplTest {
    private static final Long ID = 1L;
    private static final String USER_NAME = "John";
    private static final String USER_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        authenticationService =
                new AuthenticationServiceImpl(userService, passwordEncoder, userMapper);
    }

    @Test
    public void testRegister() {
        User user = createUser();
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(TEST_EMAIL);
        userRequestDto.setFirstName(USER_NAME);
        userRequestDto.setLastName(USER_LAST_NAME);
        userRequestDto.setPassword(PASSWORD);

        when(userMapper.toModel(userRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userService.save(user)).thenReturn(user);

        User registeredUser = authenticationService.register(userRequestDto);

        assertNotNull(registeredUser);
        assertEquals(User.Role.CUSTOMER, registeredUser.getRole());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userService, times(1)).save(user);
    }

    @Test
    public void testLoginWithValidCredentials() throws AuthenticationException {
        String login = TEST_EMAIL;
        String password = PASSWORD;
        User user = createUser();

        when(userService.findByEmail(login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        User loggedInUser = authenticationService.login(login, password);

        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        String login = TEST_EMAIL;
        String password = PASSWORD;
        User user = createUser();

        when(userService.findByEmail(login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authenticationService.login(login, password));
    }

    private User createUser() {
        return new User()
                .setId(ID)
                .setFirstName(USER_NAME)
                .setLastName(USER_LAST_NAME)
                .setEmail(TEST_EMAIL)
                .setPassword(PASSWORD)
                .setRole(User.Role.CUSTOMER);
    }
}