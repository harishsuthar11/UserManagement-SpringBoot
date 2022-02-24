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
import org.springframework.http.HttpStatus;
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

    private static Logger logger = Logger.getLogger(WalletController.class);

    //Authentication

    public String getUsernameByToken(){
        logger.debug("Authenticating the User");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("Username of Authenticated User"+username);
        return username;
    }

    //Create Wallet

    @PostMapping(value = "/wallet/{mobilenumber}")
    public ResponseObject createWallet(@PathVariable String mobilenumber) {
        logger.info("Into the Create Wallet Method");
        try {

                //Find the user corresponding to Mobilenumber
            logger.debug("Finding User Corresponding to Mobile Number");
            User user = userService.findByMobileno(mobilenumber);

            String requestTokenUsername = getUsernameByToken();
            if(user.getUsername().compareTo(requestTokenUsername)!=0){
                logger.error("Unauthorized !!! Use Your Own Token");
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
                logger.info("Wallet Created");
                return new ResponseObject(HttpStatus.OK, "Wallet Created");
            }
                //Else throw error wallet already exist
            else {
                logger.warn("Wallet Already exist");
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Wallet Already exist");
            }
        }
        catch (Exception e) {
            logger.error("User not found corresponding to Mobile Number");
            return new ResponseObject(HttpStatus.NOT_FOUND, "User not found !!!");

        }


    }

    //Add Money to Wallet

    @RequestMapping(path = "/wallet/{walletid}/{balance}", method = RequestMethod.POST)
    public ResponseObject addMoneyInWallet(@PathVariable String walletid, @PathVariable float balance) {

        logger.info("Into the Method Add Money to Wallet");

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
            logger.info("Saving the Transaction");
            transactionService.createTransaction(transaction);

            logger.info("Money Added to the Wallet");
            return new ResponseObject(HttpStatus.ACCEPTED, "Money Added to Wallet Successfully");

        }
        catch (Exception e)
        {
            logger.error("Money Not Added to the Wallet");
            throw new BadRequestException("Failure!!!!");

        }
    }


    @RequestMapping(path = "/wallet/{walletid}/transactions", method = RequestMethod.GET)

    public List<Transaction> getAllTransaction(@PathVariable String walletid, @RequestParam int pageNo) {
        logger.info("Into the Method getting all transactions of Particular ID");
        try {
            User user = userService.findByMobileno(walletid);

            String requestTokenUsername = getUsernameByToken();
            if (user.getUsername().compareTo(requestTokenUsername) != 0) {
                logger.error("Unauthorized !!! Use Your Own Token");
                throw new BadRequestException("Authentication Failed");

             }
            }
        catch (Exception exception) {
            logger.error("Transaction ID Not Found");
            throw new BadRequestException("Not Found");

        }

        if(walletService.getWalletById(walletid)!=null){
            try{
                List<Transaction> transactions = transactionService.getAllTransactionByWalletId(walletid, pageNo).getContent();

                logger.info("Transactions Accessed Successfully");
                return transactions;

            }
            catch (Exception e) {

                logger.error("Transaction ID Not exist");
                throw new BadRequestException("No Transaction exist of that particular ID");

            }
        }

        else {
            logger.error("Wallet ID Not exist");

            throw new ResourceNotFoundException("Wallet ID Not Exist");

        }

    }



    @GetMapping(value = "/wallet")

    public List<Wallet> getWallets() {
        return walletService.getAll();

    }


}
