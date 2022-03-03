package com.example.UserAPI.controller;

import com.example.UserAPI.dto.JwtRequest;
import com.example.UserAPI.dto.JwtResponse;
import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.model.User;
import com.example.UserAPI.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    public String generateToken(User user) throws Exception {

        String username = user.getUsername();
        String password = user.getPassword();

        JwtRequest jwtRequest = new JwtRequest(username,password);
        String request = objectMapper.writeValueAsString(jwtRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        JwtResponse token = objectMapper.readValue(result,JwtResponse.class);

        String userToken = token.getJwt();

        return  userToken;


    }

    @Test
    public void createUserTest() throws Exception{

        String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";
        String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestUser))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        ResponseObject responseObject = objectMapper.readValue(resultContent,ResponseObject.class);
        Assert.assertEquals(HttpStatus.ALREADY_REPORTED,responseObject.getHttpStatus());

    }

    @Test
    public void getUserByIdTest() throws Exception{

        Long id = 1L;

        String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";
        String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));



        String mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                       .contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(userService.getUserById(id).getMobilenumber(),"9783472882");



    }
}
