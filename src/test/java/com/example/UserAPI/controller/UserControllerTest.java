package com.example.UserAPI.controller;


import com.example.UserAPI.model.User;
import com.example.UserAPI.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void getUserTest() throws IOException {

        String data = new String(Files.readAllBytes(Paths.get("src/test/java/com/example/UserAPI/json/UserRequest.json")));

        User user = objectMapper.readValue(data,User.class);

        Mockito.when(MockMvcRequestBuilders.get("/user"));


    }
}
