package com.example.crudoperation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data //internally uses getter ,setter etc
@Entity
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="username",nullable = false)
    private String username;
    @Column(name="firstname",nullable = false)
    private String firstname;
    @Column(name="lastname",nullable = false)
    private String lastname;
    @Column(name="email",nullable = false)
    private String email;
    @Column(name="mobile",nullable = false)
    private String mobile;
    @Column(name="address1",nullable = false)
    private String address1;
    @Column(name="address2")
    private String address2;


}
