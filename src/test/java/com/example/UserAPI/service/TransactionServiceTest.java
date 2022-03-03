package com.example.UserAPI.service;


import com.example.UserAPI.repository.TransactionRepository;
import com.example.UserAPI.model.Transaction;
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
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;



    @Test
    public void createTransactionTest() throws IOException {

        String transactionPath = "src/test/java/com/example/UserAPI/json/TransactionDetails.json";
        String requestTransaction = new String(Files.readAllBytes(Paths.get(transactionPath)));
        Transaction transaction = new ObjectMapper().readValue(requestTransaction, Transaction.class);
        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
        Assert.assertEquals(transactionService.createTransaction(transaction),transaction);

    }


}
