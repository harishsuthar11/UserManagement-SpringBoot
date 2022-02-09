package com.example.UserAPI.model;


import lombok.Data;

import javax.persistence.*;

@Entity(name="wallet")
@Data
@Table(name="wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String walletid;

    private float balance;

}
