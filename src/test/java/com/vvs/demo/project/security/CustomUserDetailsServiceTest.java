package com.vvs.demo.project.security;

import com.vvs.demo.project.model.User;
import com.vvs.demo.project.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {
    private static final Long ID = 1L;
    private static final String USER_NAME = "John";
    private static final String USER_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    public void testLoadUserByUsernameWithExistingUser() {
        String email = "test@example.com";
        User user = new User()
                .setId(ID)
                .setFirstName(USER_NAME)
                .setLastName(USER_LAST_NAME)
                .setEmail(TEST_EMAIL)
                .setPassword(PASSWORD)
                .setRole(User.Role.CUSTOMER);

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsernameWithNonExistingUser() {
        String email = "nonexistent@example.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }
}
