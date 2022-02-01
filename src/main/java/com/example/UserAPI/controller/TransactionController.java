package com.example.UserAPI.controller;


import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.repository.TransactionRepository;
import com.example.UserAPI.repository.UserRepository;
import com.example.UserAPI.repository.WalletRepository;
import com.example.UserAPI.service.TransactionService;
import com.example.UserAPI.service.UserService;
import com.example.UserAPI.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class TransactionController {
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private TransactionService transactionService;
    @RequestMapping(path = "/transaction",method = RequestMethod.POST)
    public ResponseEntity<?> makeTransaction(@RequestBody Transaction transaction){
        Wallet payerWallet = walletService.getWalletById(transaction.getPayerWalletId()).orElseThrow(()->new ResourceNotFoundException("Payer Wallet ID Not exist!"));
        Wallet payeeWallet = walletService.getWalletById(transaction.getPayeeWalletId()).orElseThrow(()->new ResourceNotFoundException("Payee Wallet ID Not exist"));

        float currentBalancePayer=payerWallet.getBalance();
        if(transaction.getAmount()>currentBalancePayer){
            return new ResponseEntity<>("Insufficient Balance", HttpStatus.BAD_REQUEST);
        }
        float currentBalancePayee=payeeWallet.getBalance();
        try {
            currentBalancePayee = currentBalancePayee+transaction.getAmount();
            currentBalancePayer = currentBalancePayer- transaction.getAmount();
            payerWallet.setBalance(currentBalancePayer);
            payeeWallet.setBalance(currentBalancePayee);
            walletService.saveWallet(payeeWallet);
            walletService.saveWallet(payerWallet);
            transaction.setStatus("SUCCESS");
            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
            transactionService.createTransaction(transaction);
            return new ResponseEntity<>("Transaction Successful",HttpStatus.CREATED);
        }
        catch (Exception e){
            transaction.setStatus("FAILED");
            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
            transactionService.createTransaction(transaction);
            throw new BadRequestException("Transaction failed");
        }
    }
}

