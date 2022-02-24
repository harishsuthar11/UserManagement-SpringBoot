package com.example.UserAPI.service;


import com.example.UserAPI.dao.WalletRepository;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.model.Wallet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WalletServiceTest {

    @MockBean
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createWallet() throws IOException{

        String data = new String(Files.readAllBytes(Paths.get("src/test/java/com/example/UserAPI/json/Wallet1.json")));

        Wallet wallet = objectMapper.readValue(data,Wallet.class);
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);




    }

}
