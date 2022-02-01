package com.example.UserAPI.service;


import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    public void createTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }
    public List<Transaction> getAllTransactionByWalletId(String walletId){
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return transactions;
    }
}
