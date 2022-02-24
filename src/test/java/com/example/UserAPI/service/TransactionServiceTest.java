package com.example.UserAPI.service;


import com.example.UserAPI.dao.TransactionRepository;
import com.example.UserAPI.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createTransaction() throws IOException{

        String file = new String(Files.readAllBytes(Paths.get("src/test/java/com/example/UserAPI/json/TransactionDetails.json")));
        Transaction transaction = objectMapper.readValue(file,Transaction.class);
        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

    }


}
