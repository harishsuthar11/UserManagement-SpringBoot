package com.example.UserAPI.service;


import com.example.UserAPI.dao.UserRepository;
import com.example.UserAPI.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUser() throws IOException {

        String data = new String(Files.readAllBytes(Paths.get("src/test/java/com/example/UserAPI/json/UserRequest.json")));

        User user = objectMapper.readValue(data,User.class);
        Mockito.when(userRepository.save(user)).thenReturn(user);

    }
    @Test
    @DisplayName("Get Users")
    void getMobile() throws IOException{

        Long id = 4L;
        User user = userService.getUserById(9L);
        //System.out.println(user.getMobilenumber());
        Assertions.assertEquals(4,id);


    }

}
