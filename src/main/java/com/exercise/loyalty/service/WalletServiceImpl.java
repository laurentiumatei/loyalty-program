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
        Wallet wallet = getWallet(walletTransaction.getCustomerId());

        if (walletTransaction.getTransactionType() == WalletTransaction.TransactionType.DEBIT &&
                !walletHasEnoughPoints(wallet, walletTransaction))
        {
            throw new RuntimeException("Customer " + walletTransaction.getCustomerId() +
                    " does not have enough " + walletTransaction.getPointsType().toString().toLowerCase() +
                    " points.");
        }
    }

    @Override
    public Wallet getWallet(String customerId) {
        return walletRepository.findByCustomerId(customerId)
                .orElseGet(() -> createWallet(customerId));
    }

    private boolean walletHasEnoughPoints(Wallet wallet, WalletTransaction walletTransaction) {

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
        walletRepository.saveAndFlush(wallet);
        return wallet;
    }
}
