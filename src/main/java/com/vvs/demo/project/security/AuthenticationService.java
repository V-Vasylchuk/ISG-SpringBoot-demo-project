package com.vvs.demo.project.security;

import com.vvs.demo.project.dto.request.UserRequestDto;
import com.vvs.demo.project.exception.AuthenticationException;
import com.vvs.demo.project.model.User;

public interface AuthenticationService {
    User register(UserRequestDto userRequestDto);

    User login(String login, String password) throws AuthenticationException;

    String encodePassword(String password);
}
