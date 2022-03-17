package com.example.UserAPI.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name="wallet")
@Data
@Table(name="wallet")
public class Wallet {
    @Id
    private String walletid;

    private float balance;






}
