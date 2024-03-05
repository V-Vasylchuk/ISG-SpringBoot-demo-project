package com.vvs.demo.project.service.impl;

import com.vvs.demo.project.exception.DataProcessingException;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.repository.UserRepository;
import com.vvs.demo.project.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find user by id: " + id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(User user) {
        userRepository.findById(user.getId()).orElseThrow(() ->
                new DataProcessingException(
                        String.format("Not found user with id: %s for update", user.getId())));
        return userRepository.save(user);
    }
}