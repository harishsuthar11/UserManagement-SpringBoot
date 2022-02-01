package com.example.UserAPI.controller;


import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.model.User;
import com.example.UserAPI.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/user")
    public List<User> getUser(){
        return userService.getUsers();
    }
    @RequestMapping(value="/user/{id}")
    public User getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return user;
    }
    @PostMapping(value="/user")
    public ResponseEntity<?> createUser(@RequestBody User user){

        if(userService.findByMobileno(user.getMobilenumber())==null) {

            try {
                user.setStatus("ACTIVE");
                user.setCreateddate(new Date());
                user.setModifieddate(new Date());
                userService.saveUser(user);
                return new ResponseEntity<>("User Created", HttpStatus.CREATED);
            } catch (Exception e) {
                throw new BadRequestException("User Can't Be Created");
            }
        }
        else {
            return new ResponseEntity<>("User already exist", HttpStatus.CONFLICT);
        }
    }
    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody User user){
        user.setModifieddate(new Date());
        userService.saveUser(user);
        return new ResponseEntity<>("User updated",HttpStatus.OK);

    }
}
