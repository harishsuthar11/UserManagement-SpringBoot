package com.example.UserAPI.service;


import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.dao.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    public void createTransaction(Transaction transaction){

        transactionRepository.save(transaction);

    }
    @Transactional
    public Page<Transaction> getAllTransactionByWalletId(String walletId,int pageNo){

        Pageable pageable = PageRequest.of(pageNo,2);

        Page<Transaction> transactions = transactionRepository.findByWalletId(walletId,walletId,pageable);

        return transactions;

    }
}
