package com.example.UserAPI.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="wallet")
@Data
@Table(name="wallet")
public class Wallet {
    @Id
    private String walletid;

    private float balance;
}
