package com.example.UserAPI.dao;

import com.example.UserAPI.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


   // @Query("SELECT t from transaction t where t.payerWalletId=?1")
    Page<Transaction> findByPayerWalletIdOrPayeeWalletId(String payerwalletId,String payeeWalletId,Pageable pageable);

    public Transaction findByTransactionId(Long transactionId);
}
