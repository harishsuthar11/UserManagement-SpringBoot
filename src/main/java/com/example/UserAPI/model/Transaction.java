package com.example.UserAPI.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name="transaction")
@Data
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "payerWalletId",unique = true,nullable = false)
    private String payerWalletId;

    @Column(name = "payeeWalletId",unique = true,nullable = false)
    private String payeeWalletId;

    @Column(name = "amount")
    private float amount;

    private String status;
    private Timestamp timestamp;




}
