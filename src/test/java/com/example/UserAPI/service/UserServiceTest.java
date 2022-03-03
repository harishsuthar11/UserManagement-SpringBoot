package com.example.UserAPI.service;

import com.example.UserAPI.repository.UserRepository;
import com.example.UserAPI.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    //Test for Create User
    @Test
    public void createUserTest() throws IOException{

        String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";
        String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));

        User user= new ObjectMapper().readValue(requestUser, User.class);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        Assert.assertEquals(userService.saveUser(user),user);

    }
    //Test for Find By UserName
    @Test
    public void findByUsernameTest() throws IOException{
        String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";

        String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));

        User user= new ObjectMapper().readValue(requestUser, User.class);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        Assert.assertEquals(userService.findByUsername(user.getUsername()),user);

    }


    //Test for Find By Mobile
    @Test
    public void findByMobileTest() throws IOException{

        String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";

        String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));

        User user= new ObjectMapper().readValue(requestUser, User.class);

        Mockito.when(userRepository.findByMobilenumber(user.getMobilenumber())).thenReturn(user);

        Assert.assertEquals(userService.findByMobileno(user.getMobilenumber()),user);

    }

    //Test For Delete User

    @Test
    public void deleteUserTest() throws IOException{

        String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";

        String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));

        User user= new ObjectMapper().readValue(requestUser, User.class);

        userService.deleteUser(user.getId());

        Mockito.verify(userRepository,Mockito.times(1)).deleteById(user.getId());


    }





}
