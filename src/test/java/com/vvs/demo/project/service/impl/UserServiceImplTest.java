package com.vvs.demo.project.service.impl;

import com.vvs.demo.project.model.User;
import com.vvs.demo.project.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long ID = 1L;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void save() {
        User user = createUser();
        String encodedPassword = "password";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    void testFindById_ExistingId() {
        Long id = ID;
        User user = createUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User retrievedUser = userService.findById(id);

        assertNotNull(retrievedUser);
        assertEquals(user, retrievedUser);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NonExistingId() {
        Long id = ID;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testFindByEmail_ExistingEmail() {
        String email = TEST_EMAIL;
        User user = createUser();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.findByEmail(email);

        assertTrue(retrievedUser.isPresent());
        assertEquals(user, retrievedUser.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_NonExistingEmail() {
        String email = TEST_EMAIL;
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = userService.findByEmail(email);

        assertFalse(retrievedUser.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testDelete() {
        Long id = ID;
        doNothing().when(userRepository).deleteById(id);

        assertDoesNotThrow(() -> userService.delete(id));
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAll() {
        List<User> users = Collections.singletonList(createUser());
        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.findAll();

        assertNotNull(retrievedUsers);
        assertEquals(users, retrievedUsers);
        verify(userRepository, times(1)).findAll();
    }

    private User createUser() {
        return new User()
        .setId(ID)
        .setFirstName("John")
        .setLastName("Doe")
        .setEmail("john.doe@example.com")
        .setPassword("password")
        .setRole(User.Role.CUSTOMER);
    }
}