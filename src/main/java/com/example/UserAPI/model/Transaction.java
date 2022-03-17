package com.example.UserAPI.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


@NoArgsConstructor
@Entity(name="transaction")
@Data
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    @Column(name = "payerWalletId",nullable = false)

    private String payerWalletId;

    @Column(name = "payeeWalletId",nullable = false)

    private String payeeWalletId;

    @Column(name = "amount")
    private float amount;

    private String status;
    private Timestamp timestamp;




}
