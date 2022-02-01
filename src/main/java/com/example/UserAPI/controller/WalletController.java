package com.example.UserAPI.controller;

import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.model.User;
import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.service.TransactionService;
import com.example.UserAPI.service.UserService;
import com.example.UserAPI.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
    @RequestMapping(path = "/wallet/{mobilenumber}",method = RequestMethod.POST)
    public ResponseEntity<?> createWallet(@PathVariable String mobilenumber){
        User user = userService.findByMobileno(mobilenumber);
        try {

            Optional<Wallet> tempWallet = walletService.getWalletById(mobilenumber);
            if(!tempWallet.isPresent()){
                Wallet wallet = new Wallet();
                wallet.setWalletid(mobilenumber);
                wallet.setBalance(0.0F);
                walletService.saveWallet(wallet);
                user.setWallet(wallet);
                userService.saveUser(user);
                return new ResponseEntity<>("Wallet Created Successfully", HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>("WalletId already exist",HttpStatus.CONFLICT);
            }
        }
        catch (Exception e){
            throw new ResourceNotFoundException("User not found!!");
        }
    }
    @RequestMapping(path = "/wallet/{walletid}/transactions",method = RequestMethod.GET)
    public List<Transaction> getAllTransaction(@PathVariable String walletid){

        if(walletService.getWalletById(walletid)!=null){
            try {
                List<Transaction> transactions = transactionService.getAllTransactionByWalletId(walletid);
                return transactions;
            }
            catch (Exception e){
                throw new BadRequestException("No Transaction exist of that particular ID");
            }
        }
        else{
            throw new ResourceNotFoundException("Wallet ID Not Exist");
        }
    }
    @RequestMapping(path = "/wallet/{walletid}/{balance}",method = RequestMethod.POST)
    public ResponseEntity<?> addMoneyInWallet(@PathVariable String walletid,@PathVariable float balance){
        Wallet wallet = walletService.getWalletById(walletid).orElseThrow(()->new ResourceNotFoundException("WalletID Not Exist"));
        try {
            wallet.setBalance(balance);
            walletService.saveWallet(wallet);
            Transaction transaction = new Transaction();
            transaction.setAmount(balance);
            transaction.setStatus("SUCCESS");
            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
            transaction.setPayerWalletId(walletid);
            transaction.setPayeeWalletId(walletid);
            transactionService.createTransaction(transaction);
            return new ResponseEntity<>("Money Added to Wallet Successfully",HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            throw new BadRequestException("Failure!!!!");
        }
    }
}
