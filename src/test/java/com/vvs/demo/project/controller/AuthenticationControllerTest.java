package com.vvs.demo.project.controller;

import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.exception.AuthenticationException;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.security.AuthenticationService;
import com.vvs.demo.project.security.jwt.JwtTokenProvider;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testRegister() throws Exception {
        User user = createUser();
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("test@example.com");
        userRequestDto.setFirstName("John");
        userRequestDto.setLastName("Doe");
        userRequestDto.setPassword("password");
        Mockito.when(authenticationService.register(userRequestDto)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"password\":\"password\",\"role\":\"CUSTOMER\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testLogin() throws Exception {
        User user = createUser();
        Mockito.when(authenticationService.login(user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(jwtTokenProvider.createToken(user.getEmail(), Collections.singletonList(user.getRole().name())))
                .thenReturn("token");
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        Mockito.when(authenticationService.login(any(String.class), any(String.class)))
                .thenThrow(new AuthenticationException("Login With Invalid Credentials"));
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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