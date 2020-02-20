package com.exercise.loyalty.service;

import com.exercise.loyalty.helper.DateTimeHelper;
import com.exercise.loyalty.model.Wallet;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.model.WalletTransaction.TransactionType;
import com.exercise.loyalty.repository.WalletRepository;
import com.exercise.loyalty.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Repository
@Transactional
public class WalletServiceImpl implements WalletService {

    private static final int WEEKS_SINCE_LAST_TRANSACTION = 5;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    public void addToWallet(WalletTransaction walletTransaction) {
        Wallet wallet = getWallet(walletTransaction.getCustomerId());

        if (walletTransaction.getTransactionType() == TransactionType.DEBIT &&
                !walletHasEnoughPoints(wallet, walletTransaction)) {
            throw new RuntimeException("Customer " + walletTransaction.getCustomerId() +
                    " does not have enough " + walletTransaction.getPointsType().toString().toLowerCase() +
                    " points.");
        }
        BigDecimal pointsToAdd = getPointsToAdd(walletTransaction);
        switch (walletTransaction.getPointsType()) {
            case AVAILABLE:
                wallet.setAvailablePoints(wallet.getAvailablePoints().add(pointsToAdd));
                break;
            case PENDING:
                wallet.setPendingPoints(wallet.getPendingPoints().add(pointsToAdd));
                break;
            default:
                throw new RuntimeException("Points type not supported: " + walletTransaction.getPointsType());
        }
        walletRepository.save(wallet);
    }

    @Override
    public Wallet getWallet(String customerId) {
        Wallet wallet = walletRepository.findByCustomerId(customerId).orElseGet(() -> createWallet(customerId));
        if (!isFlat(wallet) && isPreviousCreditPendingPointsTransactionTooOld(customerId)) {
            return resetWallet(wallet);
        }

        return wallet;
    }

    private Wallet resetWallet(Wallet wallet) {
        wallet.setPendingPoints(BigDecimal.ZERO);
        wallet.setAvailablePoints(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    private boolean walletHasEnoughPoints(Wallet wallet, WalletTransaction walletTransaction) {

        switch (walletTransaction.getPointsType()) {
            case PENDING:
                return wallet.getPendingPoints().compareTo(walletTransaction.getPointsAmount()) >= 0;
            case AVAILABLE:
                return wallet.getAvailablePoints().compareTo(walletTransaction.getPointsAmount()) >= 0;
            default:
                throw new RuntimeException("Points type not supported: " + walletTransaction.getPointsType());
        }
    }

    private Wallet createWallet(String customerId) {
        Wallet wallet = new Wallet();
        wallet.setCustomerId(customerId);
        wallet.setPendingPoints(BigDecimal.ZERO);
        wallet.setAvailablePoints(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    private BigDecimal getPointsToAdd(WalletTransaction walletTransaction) {
        switch (walletTransaction.getTransactionType()) {
            case DEBIT:
                return walletTransaction.getPointsAmount().negate();
            case CREDIT:
                return walletTransaction.getPointsAmount();
            default:
                throw new RuntimeException("Transaction type not supported: " + walletTransaction.getTransactionType());
        }
    }

    private boolean isPreviousCreditPendingPointsTransactionTooOld(String customerId) {        
        Optional<WalletTransaction> lastTransaction =
        		walletTransactionRepository.findFirstByCustomerIdAndPointsTypeAndTransactionTypeOrderByTimestampDesc(
        				customerId, WalletTransaction.PointsType.PENDING, TransactionType.CREDIT);
        
        if (!lastTransaction.isPresent())
        {
        	return false;
        }

        LocalDateTime lastTransactionDateTime = DateTimeHelper.convertToLocalDateTime(lastTransaction.get().getTimestamp());
        long weeks = ChronoUnit.WEEKS.between(lastTransactionDateTime, LocalDateTime.now());

        return weeks > WEEKS_SINCE_LAST_TRANSACTION;
    }

    private boolean isFlat(Wallet wallet) {
        return wallet.getPendingPoints().compareTo(BigDecimal.ZERO) == 0 &&
                wallet.getAvailablePoints().compareTo(BigDecimal.ZERO) == 0;
    }
}