package com.example.UserAPI.controller;


import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.model.User;
import com.example.UserAPI.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    String topic = "quickstart-events";

    @RequestMapping(value="/user")
    public ResponseEntity<List<User>> getUser(@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
        return new ResponseEntity<List<User>>(userService.getUsers(pageNumber,pageSize),HttpStatus.OK);
    }
    @RequestMapping(value="/user/{id}")
    public User getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return user;
    }
    @RequestMapping(value = "/user/filter")
    public ResponseEntity<List<User>> getUsersByKeyword(@RequestParam String keyword){
        return new ResponseEntity<List<User>>(userService.getUsersByKeyword(keyword),HttpStatus.OK);
    }
    @PostMapping(value="/create")
    public ResponseEntity<?> createUser(@RequestBody User user){

        if(userService.findByMobileno(user.getMobilenumber())==null) {

            try {
                user.setStatus("ACTIVE");
                user.setCreateddate(new Date());
                user.setModifieddate(new Date());
                userService.saveUser(user);
                kafkaTemplate.send(topic,"User Created Successfully");
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
