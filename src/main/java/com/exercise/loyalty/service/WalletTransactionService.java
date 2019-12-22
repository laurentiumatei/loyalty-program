package com.exercise.loyalty.service;

import com.exercise.loyalty.model.WalletTransaction;

import java.util.List;

public interface WalletTransactionService {
    void addWalletTransaction(WalletTransaction walletTransaction);
    List<WalletTransaction> getTransactions(String customerId);
}
