package com.example.UserAPI.repository;

import com.example.UserAPI.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT t from transaction t where t.payerWalletId=?1")
    List<Transaction> findByWalletId(String walletId);
}
