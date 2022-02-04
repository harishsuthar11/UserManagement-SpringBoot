package com.example.UserAPI.service;

import com.example.UserAPI.model.Wallet;
import com.example.UserAPI.dao.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public void saveWallet(Wallet wallet){
        walletRepository.save(wallet);

    }

    public Optional<Wallet> getWalletById(String walletid){
        return walletRepository.findById(walletid);
    }
}
