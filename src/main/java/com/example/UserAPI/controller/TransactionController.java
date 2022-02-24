package com.example.UserAPI.controller;


import com.example.UserAPI.dao.TransactionRepository;
import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.service.TransactionService;
import com.example.UserAPI.service.UserService;
import com.example.UserAPI.service.WalletService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class TransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    private static Logger logger = Logger.getLogger(TransactionController.class);

    @RequestMapping(path = "/transaction",method = RequestMethod.POST)

    public ResponseObject makeTransaction(@RequestBody Transaction transaction){
        //GET Wallet Corresponding to Transaction
        logger.debug("Getting Wallet ID of Payer and Payees");
        String payer_walletId = transaction.getPayerWalletId();
        String payee_walletId = transaction.getPayeeWalletId();

        Wallet payerWallet = walletService.getWalletById(transaction.getPayerWalletId()).orElseThrow(()->new ResourceNotFoundException("Payer Wallet ID Not exist!"));
        Wallet payeeWallet = walletService.getWalletById(transaction.getPayeeWalletId()).orElseThrow(()->new ResourceNotFoundException("Payee Wallet ID Not exist"));
        logger.debug("Getting Balance of the Payer");
        float currentBalancePayer=payerWallet.getBalance();

        if(transaction.getAmount()>currentBalancePayer){
            logger.warn("Insufficient Balance");
            return new ResponseObject(HttpStatus.BAD_REQUEST,"Insufficient Balance");
        }

        float currentBalancePayee=payeeWallet.getBalance();


        Wallet tempWallet1= payerWallet;
        Wallet tempWallet2 = payeeWallet;

        try {
            logger.debug("Credit amount to the Payer Wallet and Debit to Payees");
            currentBalancePayee = currentBalancePayee+transaction.getAmount();
            currentBalancePayer = currentBalancePayer- transaction.getAmount();
            payerWallet.setBalance(currentBalancePayer);
            payeeWallet.setBalance(currentBalancePayee);
            transaction.setStatus("SUCCESS");
            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));

            transactionService.createTransaction(transaction);

            walletService.updateWallet(payeeWallet);
            walletService.updateWallet(payerWallet);
            logger.info("Transaction Successful");
            return new ResponseObject(HttpStatus.CREATED,"Transaction Successful!");
        }

        catch (Exception e){

            walletService.updateWallet(tempWallet1);
            walletService.updateWallet(tempWallet2);
            transaction.setStatus("FAILED");
            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
            transactionService.createTransaction(transaction);
            logger.error("Transaction Failed");
            throw new BadRequestException("Transaction failed");

        }
    }

    @RequestMapping(path = "/transaction",method = RequestMethod.GET)

    public Transaction getTransactionById(@RequestParam Long transactionId){

        Transaction transaction;

        try{
             logger.debug("Getting Transaction by TransactionId");
             transaction = transactionRepository.findByTransactionId(transactionId);

        }

        catch (Exception e){
            logger.error("Transaction ID Not exist");
            throw new ResourceNotFoundException("TransactionID not exist");

        }

        return transaction;

    }
}

