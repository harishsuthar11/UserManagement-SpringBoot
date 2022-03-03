package com.example.UserAPI.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "user")
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username",nullable = false,unique = true)
    private String username;

    @Column(name="firstname",nullable = false)
    private String firstname;

    @Column(name="lastname",nullable = false)
    private String lastname;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "mobilenumber",nullable = false,unique = true)
    private String mobilenumber;

    @Column(name = "address1")
    private String address1;

    @Column(name="address2")
    private String address2;

    @Column(name = "password")
    private String password;

    private String status;
    private Date createddate;
    private Date modifieddate;
    //one wallet per user
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "walletid",referencedColumnName = "walletid")
    private Wallet wallet;


}
