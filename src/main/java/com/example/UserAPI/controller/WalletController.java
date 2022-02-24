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
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //Authentication

    public String getUsernameByToken(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return username;
    }

    //Create Wallet

    @PostMapping(value = "/wallet/{mobilenumber}")
    public ResponseObject createWallet(@PathVariable String mobilenumber) {
        try {

                //Find the user corresponding to Mobilenumber
                User user = userService.findByMobileno(mobilenumber);

                String requestTokenUsername = getUsernameByToken();
                if(user.getUsername().compareTo(requestTokenUsername)!=0){

                    return new ResponseObject(HttpStatus.UNAUTHORIZED,"Authentication Failed");
                }

                //Fetch the wallet of particular mobile number

                Optional<Wallet> wallet1 = walletService.getWalletById(mobilenumber);


                //if it's not present then create it
                if (!wallet1.isPresent()) {

                    Wallet wallet = new Wallet();
                    wallet.setWalletid(mobilenumber);
                    wallet.setBalance(0.0F);
                    walletService.saveWallet(wallet);
                    user.setWallet(wallet);
                    userService.saveUser(user);

                    return new ResponseObject(HttpStatus.OK, "Wallet Created");
                }
                //Else throw error wallet already exist
                else {
                    return new ResponseObject(HttpStatus.BAD_REQUEST, "Wallet Already exist");
                }
            } catch (Exception e) {
                return new ResponseObject(HttpStatus.NOT_FOUND, "User not found !!!");
            }


    }

    //Add Money to Wallet

    @RequestMapping(path = "/wallet/{walletid}/{balance}", method = RequestMethod.POST)
    public ResponseObject addMoneyInWallet(@PathVariable String walletid, @PathVariable float balance) {

        Wallet wallet = walletService.getWalletById(walletid).orElseThrow(() -> new ResourceNotFoundException("WalletID Not Exist"));

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

            return new ResponseObject(HttpStatus.ACCEPTED, "Money Added to Wallet Successfully");

        }
        catch (Exception e)
        {

            throw new BadRequestException("Failure!!!!");

        }
    }


    @RequestMapping(path = "/wallet/{walletid}/transactions", method = RequestMethod.GET)

    public List<Transaction> getAllTransaction(@PathVariable String walletid, @RequestParam int pageNo) {


            try {
                User user = userService.findByMobileno(walletid);

                String requestTokenUsername = getUsernameByToken();
                if (user.getUsername().compareTo(requestTokenUsername) != 0) {

                    throw new BadRequestException("Authentication Failed");
                }
            }
            catch (Exception exception) {
                throw new BadRequestException("Not Found");
            }
            if(walletService.getWalletById(walletid)!=null){
                try{

                List<Transaction> transactions = transactionService.getAllTransactionByWalletId(walletid, pageNo).getContent();
                return transactions;
               }
                catch (Exception e) {
                throw new BadRequestException("No Transaction exist of that particular ID");}
        }
            else {
            throw new ResourceNotFoundException("Wallet ID Not Exist");
        }
    }



    @GetMapping(value = "/wallet")

    public List<Wallet> getWallets() {
        return walletService.getAll();

    }


}
