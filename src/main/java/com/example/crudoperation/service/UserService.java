package com.example.crudoperation.service;

import com.example.crudoperation.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getallUser();
    User getUser(long id);
    User updateUser(User user,long id);
    void deleteUser(long id);
    //User findByEmail(String email);
//    String doesExist(User user);
}
