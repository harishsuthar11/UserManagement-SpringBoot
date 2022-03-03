package com.example.UserAPI.controller;


import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.model.User;
import com.example.UserAPI.service.UserService;
import com.example.UserAPI.validation.Validation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    String topic = "user";

    private static Logger logger = Logger.getLogger(UserController.class);


    //This Mapping is to get all the users
    //Request URL : http://localhost:8080/user?pageNumber=<pageNumber>&pageSize=<pageSize>

    @RequestMapping(value="/user")
    public ResponseEntity<List<User>> getUser(@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
        //kafkaTemplate.send(topic,"Getting User");
        logger.debug("Getting Users ...");
        return new ResponseEntity<List<User>>(userService.getUsers(pageNumber,pageSize),HttpStatus.OK);

    }

    //This mapping is to get user id wise

    //Request URL : http://localhost:8080/user/<id>
    @RequestMapping(value="/user/{id}")
    public User getUserById(@PathVariable Long id){

        logger.debug("Getting User Details having UserID "+id);

        User user = userService.getUserById(id);
        logger.info("User found with Name "+user.getFirstname()+" "+user.getLastname());
        return user;

    }

    //This mapping is to get user by keywords

    //Request URL:http://localhost:8080/user/filter?keyword=<keyword>

    @RequestMapping(value = "/user/filter")
    public ResponseEntity<List<User>> getUsersByKeyword(@RequestParam String keyword){
        logger.debug("Getting Users with the Keyword "+keyword);
        return new ResponseEntity<List<User>>(userService.getUsersByKeyword(keyword),HttpStatus.OK);

    }
    //This mapping is to add new user
    //This will validate email and mobile number before creating the user

    //URL:http://localhost:8080/create

    @PostMapping(value="/create")
    public ResponseObject createUser(@RequestBody User user){

        logger.debug("Accessing Create User API");


        if(userService.findByMobileno(user.getMobilenumber())==null) {

            try {
                user.setStatus("ACTIVE");

                user.setCreateddate(new Date());

                user.setModifieddate(new Date());

                userService.saveUser(user);
//                kafkaTemplate.send(topic,"User Created Successfully");
                logger.info("User Created with name "+user.getFirstname()+" "+user.getLastname());
       //         kafkaTemplate.send(topic,user.getEmail());
                return new ResponseObject( HttpStatus.CREATED,"User Created");

            }
            catch (Exception e) {

                throw new BadRequestException("User Can't Be Created");

            }

        }

        else
        {
            logger.info("Username Already Exist "+user.getUsername());
            return new ResponseObject(HttpStatus.ALREADY_REPORTED,"User already exist");

        }
    }

    @PutMapping(value = "/user/{id}")
    public ResponseObject updateUser(@PathVariable Long id,@RequestBody User user){

        user.setModifieddate(new Date());
        userService.saveUser(user);
        return new ResponseObject(HttpStatus.OK,"Updated Successfully");

    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseObject deleteUser(@PathVariable long id){

        try {

            logger.debug("user deleted");
            userService.deleteUser(id);

        }
        catch (Exception e){

            throw new BadRequestException("Can't be deleted");

        }
       return  new ResponseObject(HttpStatus.ACCEPTED,"User Deleted");


    }
}
