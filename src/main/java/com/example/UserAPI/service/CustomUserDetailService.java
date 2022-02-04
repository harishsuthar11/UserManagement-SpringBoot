package com.example.UserAPI.service;


import com.example.UserAPI.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private  UserService userService;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            com.example.UserAPI.model.User user = userService.findByUsername(username);
            return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Not Found");
        }

    }
}
