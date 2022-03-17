package com.example.UserAPI.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    @Email(message = "User need valid Email")
    private String email;

    @Pattern(regexp = "[0-9]+",message = "Enter Only Digits")
    @Size(min = 10,max = 10,message = "Enter Only 10 digit mobile Number")
    @Column(name = "mobilenumber",nullable = false,unique = true)
    private String mobilenumber;

    @Column(name = "address")
    private String address;



    private boolean isActiveWallet;


    @Column(name = "password")
    private String password;

    private String status;
    private Date createddate;

    //one wallet per user
    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;






}
