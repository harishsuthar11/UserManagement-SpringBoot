package com.example.UserAPI.service;


import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.model.User;
import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.repository.WalletRepository;
import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.repository.TransactionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    private static Logger logger = Logger.getLogger(TransactionService.class.getName());

    public Transaction createTransaction(Transaction transaction){

        logger.info("Created Transaction with TxnId :"+transaction.getTransactionId());

        transactionRepository.save(transaction);

        return transaction;



    }

    public Page<Transaction> getAllTransactionByWalletId(String walletId,int pageNo){

        Pageable pageable = PageRequest.of(pageNo,2);
        Page<Transaction> transactions = transactionRepository.findByPayerWalletIdOrPayeeWalletId(walletId,walletId,pageable);
        return transactions;

    }

    public Page<Transaction> getAllTransactionByUserId(Long id,int pageNo){

        User user = userService.getUserById(id);
        Wallet wallet = walletService.getWalletById(user.getMobilenumber());
        Page<Transaction> transactions = getAllTransactionByWalletId(wallet.getWalletid(),pageNo);
        logger.info("Retrieving All Transaction of WalletId "+wallet.getWalletid());
        return transactions;

    }

    public Transaction getTransactionByTransactionId(Long transactionId){

        logger.info("Fetching the Transaction with Transaction Id "+transactionId);

        return transactionRepository.findByTransactionId(transactionId);
    }

    public ResponseObject transferMoney(Transaction transaction){

        Wallet payerWallet = walletService.getWalletById(transaction.getPayerWalletId());

        Wallet payeeWallet = walletService.getWalletById(transaction.getPayeeWalletId());

        if(payerWallet.getBalance()<transaction.getAmount())
            return new ResponseObject(HttpStatus.BAD_REQUEST,"Insufficient Balance");

        Wallet tempPayerWallet = payerWallet;
        Wallet tempPayeeWallet = payeeWallet;

        try{
            payerWallet.setBalance(payerWallet.getBalance()-transaction.getAmount());
            payeeWallet.setBalance(payeeWallet.getBalance()+transaction.getAmount());

            walletService.updateWallet(payerWallet);
            walletService.updateWallet(payeeWallet);

            Transaction transaction1 = new Transaction();
            transaction1.setAmount(transaction.getAmount());
            transaction1.setPayeeWalletId(transaction.getPayeeWalletId());
            transaction1.setPayerWalletId(transaction.getPayerWalletId());
            transaction1.setStatus("SUCCESS");
            transaction1.setTimestamp(Timestamp.from(Instant.now()));
            transactionRepository.save(transaction1);
            logger.info("New Transaction with Id "+transaction1.getTransactionId());

            return new ResponseObject(HttpStatus.ACCEPTED,"Money Transferred Successfully");
        }
        catch (Exception e){
            logger.warn("Transaction Failed ");

            walletService.updateWallet(tempPayeeWallet);
            walletService.updateWallet(tempPayerWallet);

            Transaction transaction1 = new Transaction();
            transaction1.setAmount(transaction.getAmount());
            transaction1.setPayeeWalletId(transaction.getPayeeWalletId());
            transaction1.setPayerWalletId(transaction.getPayerWalletId());
            transaction1.setStatus("FAILED");
            transaction1.setTimestamp(Timestamp.from(Instant.now()));
            transactionRepository.save(transaction1);

            return new ResponseObject(HttpStatus.BAD_REQUEST,"Transaction Failed ");
        }
    }
}
