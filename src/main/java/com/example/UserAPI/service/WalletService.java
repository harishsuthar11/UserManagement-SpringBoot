package com.example.UserAPI.service;

import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet saveWallet(Wallet wallet){

        return walletRepository.save(wallet);

    }

    public Optional<Wallet> getWalletById(String walletid){

        return walletRepository.findById(walletid);

    }

    public Wallet updateWallet(Wallet wallet){

        walletRepository.save(wallet);
        return wallet;

    }

    public List<Wallet> getAll(){

        return walletRepository.findAll();

    }


}
