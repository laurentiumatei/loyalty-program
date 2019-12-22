package com.exercise.loyalty.service;

import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletService walletService;

    @Autowired
    public WalletTransactionServiceImpl(
            WalletTransactionRepository walletTransactionRepository,
            WalletService walletService) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletService = walletService;
    }

    @Override
    public void addWalletTransaction(WalletTransaction walletTransaction)
    {
        walletService.addToWallet(walletTransaction);
        walletTransactionRepository.save(walletTransaction);
    }

    @Override
    public List<WalletTransaction> getTransactions(String customerId) {
        return walletTransactionRepository.findAllByCustomerIdOrderByTimestampDesc(customerId);
    }

}
