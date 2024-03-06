package com.vvs.demo.project.controller;

import com.vvs.demo.project.dto.mapper.UserMapper;
import com.vvs.demo.project.dto.response.UserResponseDto;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.security.AuthenticationService;
import com.vvs.demo.project.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, authenticationService, userMapper);
    }

    @Test
    void testValidAuthentication() {
        User user = createUser();
        UserResponseDto userResponseDto = createUserResponseDto();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto response = userController.get(authentication);

        assertEquals(userResponseDto, response);

        verify(userService, times(1)).findByEmail(anyString());
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testUpdateRole() {
        User user = createUser();

        UserResponseDto userResponseDto = createUserResponseDto();

        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.update(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto response = userController.updateRole(1L, User.Role.MANAGER.toString());

        assertEquals(userResponseDto, response);

        verify(userService, times(1)).findById(anyLong());
        verify(userService, times(1)).update(any(User.class));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    private UserResponseDto createUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setEmail("test@example.com");
        userResponseDto.setRole(User.Role.CUSTOMER);
        return userResponseDto;
    }

    private User createUser() {
        return new User()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("test@example.com")
                .setPassword("password")
                .setRole(User.Role.CUSTOMER);
    }
}