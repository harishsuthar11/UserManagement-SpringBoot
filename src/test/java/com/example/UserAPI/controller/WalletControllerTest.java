package com.example.UserAPI.controller;

import com.example.UserAPI.dto.JwtRequest;
import com.example.UserAPI.dto.JwtResponse;
import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
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
        Assert.assertEquals(HttpStatus.CREATED, responseObject.getHttpStatus());
    }


    @Test

    public void addMoneyTest() throws Exception {


       String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
       String walletDetail = new String(Files.readAllBytes(Paths.get(walletPath)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/wallet/9999999999/50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        ResponseObject responseObject = objectMapper.readValue(resultContent, ResponseObject.class);
        Assert.assertEquals(HttpStatus.ACCEPTED, responseObject.getHttpStatus());

    }
    @Test
    public void getWalletDetailsByIdTest() throws Exception{
        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
        String walletDetail = new String(Files.readAllBytes(Paths.get(walletPath)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/wallet/9999999999/50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }
}
