package com.vvs.demo.project.controller;

import com.vvs.demo.project.dto.mapper.UserMapper;
import com.vvs.demo.project.dto.request.LoginRequestDto;
import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.dto.response.UserResponseDto;
import com.vvs.demo.project.exception.AuthenticationException;
import com.vvs.demo.project.model.EmailDetails;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.security.AuthenticationService;
import com.vvs.demo.project.security.jwt.JwtTokenProvider;
import com.vvs.demo.project.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private static final String RECIPIENT_EMAIL = "vvs14@i.ua";
    private static final String EMAIL_SUBJECT = "New user registration";
    private static final String EMAIL_BODY = "New user registration with email: '%s'";


    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration of a new user (default role a CUSTOMER)")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userMapper.toDto(authenticationService
                .register(userRequestDto));

        emailService.sendSimpleMail(new EmailDetails()
                .setRecipient(RECIPIENT_EMAIL)
                .setSubject(EMAIL_SUBJECT)
                .setMsgBody(String.format(EMAIL_BODY,
                        userRequestDto.getEmail())));
        return userResponseDto;
    }

    @PostMapping("/login")
    @Operation(summary = "Get a JWT token for registration user")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        User user;
        try {
            user = authenticationService
                    .login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            String token = jwtTokenProvider
                    .createToken(user.getEmail(), List.of(user.getRole().name()));
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now().toString());
            body.put("token", token);
            body.put("status", HttpStatus.OK.value());
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password!");
        }
    }
}
