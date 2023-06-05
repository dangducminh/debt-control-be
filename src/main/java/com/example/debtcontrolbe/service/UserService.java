package com.example.debtcontrolbe.service;

import com.example.debtcontrolbe.model.User;
import com.example.debtcontrolbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String login(String username, String password) {
        Optional<User> user =  userRepository.findAllByUsernameAndPassword(username,password);
        if (user.isPresent()){
            return "success";
        }
        return "fail";
    }
}
