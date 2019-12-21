package com.exercise.loyalty.service;

import com.exercise.loyalty.model.WalletTransaction;

public interface WalletService {
    void addToWallet(WalletTransaction walletTransaction);
}
