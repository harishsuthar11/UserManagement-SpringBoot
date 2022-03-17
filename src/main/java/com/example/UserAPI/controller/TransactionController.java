package com.example.UserAPI.controller;


import com.example.UserAPI.repository.TransactionRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

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

    private static Logger logger = Logger.getLogger(TransactionController.class.getName());

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private static final String topic = "user";

    @RequestMapping(path = "/transaction",method = RequestMethod.POST)

    public ResponseObject transferMoney(@RequestBody Transaction transaction){

        ResponseObject responseObject = transactionService.transferMoney(transaction);
        try {
            if (responseObject.getHttpStatus().compareTo(HttpStatus.ACCEPTED)==0)
            {
                kafkaTemplate.send(topic,"Money Transferred Successfully from Wallet ID :"+transaction.getPayerWalletId()+" To Wallet ID :"+transaction.getPayeeWalletId());
            }

        }
        catch (Exception e){
            throw new RuntimeException("can't push event in kafka");
        }
        return responseObject;

    }

    @RequestMapping(path = "/transaction/{id}",method = RequestMethod.GET)
    public ResponseEntity<List<Transaction>> getTransactionById(@RequestParam Long id,@RequestParam int pageNo){

        Page<Transaction> transactions = transactionService.getAllTransactionByUserId(id,pageNo);
        logger.debug("Fetching All transaction of user Id "+id);
        return ResponseEntity.ok(transactions.getContent());

    }

    @GetMapping("/transaction")
    public String getStatusByTransactionId(@RequestParam Long transactionId){

        try{

            Transaction transaction = transactionService.getTransactionByTransactionId(transactionId);
            logger.debug("Fetching transaction Status of txn Id :"+transactionId);
            return transaction.getStatus();
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Transaction ID not exist");
        }
    }
}

