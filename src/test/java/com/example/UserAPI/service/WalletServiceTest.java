package com.example.UserAPI.service;


import com.example.UserAPI.repository.WalletRepository;
import com.example.UserAPI.model.Wallet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;



    //Create Wallet Test
    @Test

    public void createWalletTest() throws IOException{

        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
        String requestWallet = new String(Files.readAllBytes(Paths.get(walletPath)));

        Wallet wallet = new ObjectMapper().readValue(requestWallet, Wallet.class);
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);

        Assert.assertEquals(walletService.saveWallet(wallet.getWalletid()).getHttpStatus(), HttpStatus.CREATED);
        //Mockito.verify(walletRepository,Mockito.times(1)).save(wallet);
    }

    @Test

    public void addMoneyTest() throws IOException{
        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
        String requestWallet = new String(Files.readAllBytes(Paths.get(walletPath)));

        Wallet wallet = new ObjectMapper().readValue(requestWallet, Wallet.class);
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);

        Assert.assertEquals(walletService.saveWallet(wallet.getWalletid()),wallet);
        Mockito.verify(walletRepository,Mockito.times(1)).save(wallet);

    }

    @Test
    public void getwalletByIdTest() throws IOException{

        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
        String requestWallet = new String(Files.readAllBytes(Paths.get(walletPath)));
        Wallet wallet = new ObjectMapper().readValue(requestWallet, Wallet.class);
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        Wallet  wallet1 = walletService.getWalletById(wallet.getWalletid());
        Assert.assertEquals(wallet1,wallet);


    }

    @Test
    public void updateWalletTest() throws IOException{

        String walletPath = "src/test/java/com/example/UserAPI/json/WalletDetails.json";
        String requestWallet = new String(Files.readAllBytes(Paths.get(walletPath)));
        Wallet wallet = new ObjectMapper().readValue(requestWallet, Wallet.class);
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        Assert.assertEquals(walletService.updateWallet(wallet),wallet);


    }




}
