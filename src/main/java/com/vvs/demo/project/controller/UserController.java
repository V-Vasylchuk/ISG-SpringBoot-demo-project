package com.vvs.demo.project.controller;

import com.vvs.demo.project.dto.mapper.DtoMapper;
import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.dto.response.UserResponseDto;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final DtoMapper<User, UserRequestDto, UserResponseDto> userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get my profile info")
    public UserResponseDto get(String email) {
        return userMapper.toDto(userService.findByEmail(email).get());
    }

    @PostMapping
    @Operation(summary = "Create new user")
    public UserResponseDto create(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userMapper.toDto(userService.save(userMapper.toModel(userRequestDto)));
    }

    @PutMapping("/me")
    @Operation(summary = "Update profile info by email")
    public UserResponseDto update(@RequestBody @Valid UserRequestDto userRequestDto) {
        Long userId = userService.findByEmail(userRequestDto.getEmail()).get().getId();
        return userMapper.toDto(userService
                .update(userMapper
                        .toModel(userRequestDto)
                        .setId(userId)
                        .setPassword(userRequestDto.getPassword())));
    }
}
