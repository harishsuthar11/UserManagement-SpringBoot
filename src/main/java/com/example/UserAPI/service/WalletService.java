package com.example.UserAPI.service;

import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.model.User;
import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.repository.WalletRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = Logger.getLogger(WalletService.class.getName());

    public ResponseObject saveWallet(String mobileNumber) {

        try {
            User user = userService.findByMobileno(mobileNumber);
            if (user.getWallet() == null) {

                Wallet wallet = new Wallet();
                wallet.setWalletid(mobileNumber);
                wallet.setBalance(0.0F);

                user.setActiveWallet(true);
                user.setWallet(wallet);
                userService.updateUser(user);
                walletRepository.save(wallet);

                logger.info("Wallet Created  " + wallet);
                return new ResponseObject(HttpStatus.CREATED,"Wallet Created Successfullly");

            } else {
                return new ResponseObject(HttpStatus.CONFLICT, "User Has Already A Wallet");
            }
        } catch (Exception e) {
            logger.info("Wallet Can't Be Created ");
            throw new BadRequestException("Wallet Can't Be Created");
        }
    }

    public Wallet getWalletById(String walletid){
        logger.debug("Retriving Wallet Details ");

        return walletRepository.findById(walletid).orElseThrow(()->new ResourceNotFoundException("Id Not Exist"));

    }

    public ResponseObject addMoney(String mobileNumber,float amount){
        try{

            User user = userService.findByMobileno(mobileNumber);
            if(user.getWallet().getWalletid()==null){
                return new ResponseObject(HttpStatus.BAD_REQUEST,"Wallet ID Not Exist");
            }

            Wallet wallet = walletRepository.findById(mobileNumber).orElseThrow(()->new ResourceNotFoundException("Not Found"));
            wallet.setBalance(amount);
            logger.info("Money added to  wallet is "+amount);
            walletRepository.save(wallet);

            Transaction transaction = new Transaction();
            transaction.setTimestamp(Timestamp.from(Instant.now()));
            transaction.setPayerWalletId(mobileNumber);
            transaction.setPayeeWalletId(mobileNumber);
            transaction.setStatus("SUCCESS");
            transaction.setAmount(amount);
            transactionService.createTransaction(transaction);
            logger.info("Transaction Done with TxnId "+transaction.getTransactionId());

            return new ResponseObject(HttpStatus.ACCEPTED,"Money Added to Wallet Successfully");

        }
        catch (Exception e){

            throw  new BadRequestException("Can't Add Money to Wallet due to exception "+e.getLocalizedMessage());
        }
    }

    public Wallet updateWallet(Wallet wallet){

        walletRepository.save(wallet);
        return wallet;

    }

    public List<Wallet> getAll(){

        return walletRepository.findAll();

    }


}
