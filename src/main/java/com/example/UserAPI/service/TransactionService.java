package com.example.UserAPI.service;


import com.example.UserAPI.model.Transaction;
import com.example.UserAPI.dao.TransactionRepository;
import com.example.UserAPI.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//    public List<User> getUsers(int pageNumber, int pageSize) {
//
//        Pageable pageable = PageRequest.of(pageNumber,pageSize);
//
//        return userRepository.findAll(pageable).getContent();
//
//    }
    @Transactional
    public Page<Transaction> getAllTransactionByWalletId(String walletId,int pageNo){

        Pageable pageable = PageRequest.of(pageNo,2);
        Page<Transaction> transactions = transactionRepository.findByPayerWalletIdOrPayeeWalletId(walletId,walletId,pageable);
         return transactions;


    }
}
