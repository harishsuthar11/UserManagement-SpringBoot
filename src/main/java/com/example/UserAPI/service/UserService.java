package com.example.UserAPI.service;


import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.User;
import com.example.UserAPI.model.Wallet;

import com.example.UserAPI.repository.UserRepository;

import com.example.UserAPI.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User ID Not exist"));
        return user;
    }
    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }
    public void updateUser(User user){
        userRepository.save(user);
    }
    @Transactional
    public User findByMobileno(String mobilenumber){
        User user = userRepository.findByMobileno(mobilenumber);
        return user;
    }

}
