package com.example.crudoperation.service.Impl;

import com.example.crudoperation.entity.User;
import com.example.crudoperation.exception.ResourceNotFoundException;
import com.example.crudoperation.repository.UserRepository;
import com.example.crudoperation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repo;
    public UserServiceImpl(UserRepository repo){
        super();
        this.repo = repo;
    }
    public User saveUser(User user){


          return repo.save(user);

    }
    public List<User> getallUser(){
       return repo.findAll();
    }

    public User getUser(long id){
        Optional<User> user = repo.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new ResourceNotFoundException("User","id",id);
        }

    }
     public  User updateUser(User user,long id){
        //Check if given id exist or not
        User existuser = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
        existuser.setUsername(user.getUsername());
        existuser.setFirstname(user.getFirstname());
        existuser.setLastname(user.getLastname());
        existuser.setEmail(user.getEmail());
        existuser.setMobile(user.getMobile());
        existuser.setAddress1(user.getAddress1());
        existuser.setAddress2(user.getAddress2());
        //save user
        repo.save(existuser);
        return existuser;
    }
    public void deleteUser(long id){
        //Check whether the User exist or Not
        repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User","id",id));
        repo.deleteById(id);
    }
    public boolean findByEmail(String email){
        boolean user = repo.findByEmail(email);
        return user;
    }

}
