package com.example.UserAPI.controller;

import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.model.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@RestController
public class WalletController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    private static Logger logger = Logger.getLogger(WalletController.class.getName());

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private static final String topic = "user";



    //Create Wallet

    @PostMapping(value = "/wallet/{mobilenumber}")
    public ResponseObject createWallet(@PathVariable String mobilenumber) {
        logger.info("Into the Create Wallet Method");
        try{
            return walletService.saveWallet(mobilenumber);
        }
        catch (Exception e){
            throw new BadRequestException("Wallet Can't Be Created");
        }


    }

    //Add Money to Wallet

    @RequestMapping(path = "/wallet/{walletid}/{balance}", method = RequestMethod.POST)
    public ResponseObject addMoneyInWallet(@PathVariable String walletid, @PathVariable float balance) {

        logger.info("Into the Method Add Money to Wallet");
        try{
            ResponseObject responseObject = walletService.addMoney(walletid,balance);
            try{

                if(responseObject.getHttpStatus().compareTo(HttpStatus.ACCEPTED)==0){
                    kafkaTemplate.send(topic,"Money Added to Wallet with ID :"+walletid+" And Amount "+balance+" with TimeStamp "+Timestamp.from(Instant.now()));
                }

            }catch (Exception e){
                throw new BadRequestException("Can't Push Event in Kafka");
            }
            return responseObject;

        }catch (Exception e){
            throw new BadRequestException("Money Can't Be added to Wallet due to Exception "+e.getLocalizedMessage());
        }


    }


    @RequestMapping(path = "/wallet/{walletid}/transactions", method = RequestMethod.GET)

    public ResponseEntity<List<Transaction>> getAllTransaction(@PathVariable String walletid, @RequestParam int pageNo) {
        logger.info("Into the Method getting all transactions of Particular ID");
        try {

            Page<Transaction> transactions = transactionService.getAllTransactionByWalletId(walletid,pageNo);
            logger.debug("Fetching All transaction of WalletId :"+walletid);
            return ResponseEntity.ok(transactions.getContent());
        }
        catch(Exception e){

            throw new BadRequestException("Invalid Request ");

        }


    }



    @GetMapping(value = "/wallet/{mobileNumber}")

    public Wallet getWalletDetails(@PathVariable String mobileNumber) {

        return walletService.getWalletById(mobileNumber);

    }


}
//    Authentication
//
//    public String getUsernameByToken(){
//        logger.debug("Authenticating the User");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        logger.info("Username of Authenticated User "+username);
//        return username;
//    }