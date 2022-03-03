package com.example.UserAPI.controller;

import com.example.UserAPI.dto.JwtRequest;
import com.example.UserAPI.dto.JwtResponse;
import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WalletService walletService;

   public String generateToken() throws Exception {

//       String username = user.getUsername();
//       String password = user.getPassword();

       JwtRequest jwtRequest = new JwtRequest("hs11","123");
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
    public void createWalletTest() throws Exception {

        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
        String walletDetail = new String(Files.readAllBytes(Paths.get(walletPath)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/wallet/9999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletDetail))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        ResponseObject responseObject = objectMapper.readValue(resultContent, ResponseObject.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseObject.getHttpStatus());
    }


    @Test

    public void addMoneyTest() throws Exception {


//        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
//        String walletDetail = new String(Files.readAllBytes(Paths.get(walletPath)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/wallet/9999999999/50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        ResponseObject responseObject = objectMapper.readValue(resultContent, ResponseObject.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseObject.getHttpStatus());

    }
}
