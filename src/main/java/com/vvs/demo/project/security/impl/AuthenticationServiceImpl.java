package com.vvs.demo.project.security.impl;

import com.vvs.demo.project.dto.mapper.UserMapper;
import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.exception.AuthenticationException;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.security.AuthenticationService;
import com.vvs.demo.project.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User register(UserRequestDto userRequestDto) {
        User user = userMapper.toModel(userRequestDto)
                .setRole(User.Role.CUSTOMER)
                .setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user = userService.save(user);
        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> user = userService.findByEmail(login);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        throw new AuthenticationException("Incorrect username or password!!!");
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
