package com.example.crudoperation.controller;


import com.example.crudoperation.entity.User;
import com.example.crudoperation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService service;
    public UserController(UserService service){
        super();
        this.service = service;
    }
    //Create User API
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user){
     if(!service.findByEmail(user.getEmail())) {
            service.saveUser(user);
            return ResponseEntity.ok(user);
       }
       else{
          return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
       }
    }
    //build all user get api
     @GetMapping
    public List<User> getallUsers(){
      return  service.getallUser();
     }

     //GET using id
    @GetMapping("{userid}")
    public ResponseEntity<User> getUserbyID(@PathVariable("userid") long id){
     return new ResponseEntity<User>(service.getUser(id),HttpStatus.OK);
    }

    //Update User REST API
    @PutMapping("{userid}")
    public ResponseEntity<User> updateUser(@PathVariable("userid") long id,
                                            @RequestBody User user){
        return new ResponseEntity<User>(service.updateUser(user,id),HttpStatus.OK);


    }
    //Delete REST API
    @DeleteMapping("{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable("userid") long id){
        service.deleteUser(id);

        return new ResponseEntity<String>("Delete Successfully",HttpStatus.OK);

    }





}
