package com.tmsuserservice.service;

import com.tmsuserservice.config.JwtProvider;
import com.tmsuserservice.model.User;
import com.tmsuserservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUserProfile(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);

        return user;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
