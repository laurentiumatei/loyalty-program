package com.exercise.loyalty.service;

import com.exercise.loyalty.model.Wallet;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void addToWallet(WalletTransaction walletTransaction)
    {
        if (walletTransaction.getTransactionType() == WalletTransaction.TransactionType.DEBIT &&
                !walletHasEnoughPoints(walletTransaction))
        {
            throw new RuntimeException("Customer "+ walletTransaction.getCustomerId() +
                    " does not have enough " + walletTransaction.getPointsType().toString().toLowerCase() +
                    " points.");
        }
        Wallet wallet = walletRepository.findByCustomerId(walletTransaction.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cannot find wallet for customerId: " + walletTransaction.getCustomerId()));
    }

    private boolean walletHasEnoughPoints(WalletTransaction walletTransaction) {

        Wallet wallet = walletRepository.findByCustomerId(walletTransaction.getCustomerId())
                .orElseGet(() -> createWallet(walletTransaction.getCustomerId()));
        switch (walletTransaction.getPointsType()) {
            case PENDING:
                return wallet.getPendingPoints().compareTo(walletTransaction.getPointsAmount()) >= 0;
            case AVAILABLE:
                return wallet.getAvailablePoints().compareTo(walletTransaction.getPointsAmount()) >= 0;
            default:
                throw new RuntimeException("Points type not supported: "+walletTransaction.getPointsType());
        }
    }

    private Wallet createWallet(String customerId) {
        Wallet wallet = new Wallet();
        wallet.setCustomerId(customerId);
        wallet.setPendingPoints(BigDecimal.ZERO);
        wallet.setAvailablePoints(BigDecimal.ZERO);
        walletRepository.save(wallet);
        return wallet;
    }
}
