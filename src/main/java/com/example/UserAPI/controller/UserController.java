package com.example.UserAPI.controller;


import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.model.User;
import com.example.UserAPI.service.UserService;
import com.example.UserAPI.validation.Validation;
import org.slf4j.Logger;
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
    private Validation validation;
//    @Autowired
//    private KafkaTemplate<String,String> kafkaTemplate;
//    String topic = "quickstart-events";

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    //This Mapping is to get all the users
    //Request URL : http://localhost:8080/user?pageNumber=<pageNumber>&pageSize=<pageSize>

    @RequestMapping(value="/user")
    public ResponseEntity<List<User>> getUser(@RequestParam Integer pageNumber,@RequestParam Integer pageSize){

        return new ResponseEntity<List<User>>(userService.getUsers(pageNumber,pageSize),HttpStatus.OK);

    }

    //This mapping is to get user id wise

    //Request URL : http://localhost:8080/user/<id>
    @RequestMapping(value="/user/{id}")
    public User getUserById(@PathVariable Long id){

        User user = userService.getUserById(id);

        return user;

    }

    //This mapping is to get user by keywords

    //Request URL:http://localhost:8080/user/filter?keyword=<keyword>

    @RequestMapping(value = "/user/filter")
    public ResponseEntity<List<User>> getUsersByKeyword(@RequestParam String keyword){

        return new ResponseEntity<List<User>>(userService.getUsersByKeyword(keyword),HttpStatus.OK);

    }
    //This mapping is to add new user
    //This will validate email and mobile number before creating the user

    //URL:http://localhost:8080/create

    @PostMapping(value="/create")

    public ResponseEntity<?> createUser(@RequestBody User user){

        if(!validation.emailValidation(user.getEmail()))
            return new ResponseEntity<>("Email is Invalid",HttpStatus.BAD_REQUEST);

        if(!validation.mobileNumberValidate(user.getMobilenumber()))
            return new ResponseEntity<>("Mobile number is Invalid",HttpStatus.BAD_REQUEST);

        if(userService.findByMobileno(user.getMobilenumber())==null) {

            try {
                user.setStatus("ACTIVE");

                user.setCreateddate(new Date());

                user.setModifieddate(new Date());

                userService.saveUser(user);
//                kafkaTemplate.send(topic,"User Created Successfully");

                return new ResponseEntity<>("User Created", HttpStatus.CREATED);

            }
            catch (Exception e) {

                throw new BadRequestException("User Can't Be Created");

            }

        }

        else
        {

            return new ResponseEntity<>("User already exist", HttpStatus.CONFLICT);

        }
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody User user){
        user.setModifieddate(new Date());
        userService.saveUser(user);
        return new ResponseEntity<>("User updated",HttpStatus.OK);

    }

    @DeleteMapping(value = "/user/{id}")
    public void deleteUser(@PathVariable long id){

        try {
            logger.debug("user deleted");
            userService.deleteUser(id);
        }catch (Exception e){
            throw new BadRequestException("Can't be deleted");
        }



    }
}
