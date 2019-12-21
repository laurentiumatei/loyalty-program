package com.exercise.loyalty.service;

import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.model.WalletTransaction.TransactionType;
import com.exercise.loyalty.repository.WalletRepository;
import com.exercise.loyalty.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalletTransactionServiceImpl {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public WalletTransactionServiceImpl(
            WalletTransactionRepository walletTransactionRepository,
            WalletRepository walletRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletRepository = walletRepository;
    }

    public void addWalletTransaction(WalletTransaction walletTransaction)
    {
        if (walletTransaction.getTransactionType() == TransactionType.DEBIT &&
                !hasSufficientPoints(walletTransaction.getCustomerId()))
        {
            throw new RuntimeException("Customer "+ walletTransaction.getCustomerId() +
                    " does not have enough " + walletTransaction.getPointsType().toString().toLowerCase() +
                    " points.");

        }

        walletTransactionRepository.save(walletTransaction);
    }

    private boolean hasSufficientPoints(String customerId) {

        return true;
    }
}
