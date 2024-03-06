package com.vvs.demo.project.dto.mapper;

import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.dto.response.UserResponseDto;
import com.vvs.demo.project.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapper<User, UserRequestDto, UserResponseDto> {
    @Override
    public User toModel(UserRequestDto requestDto) {
        return new User()
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setEmail(requestDto.getEmail())
                .setPassword(requestDto.getPassword());
    }

    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(user.getRole());
    }
}
