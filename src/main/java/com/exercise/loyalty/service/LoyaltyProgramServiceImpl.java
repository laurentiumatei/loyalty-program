package com.exercise.loyalty.service;

import com.exercise.loyalty.component.PointsCalculator;
import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.model.WalletTransaction.PointsType;
import com.exercise.loyalty.model.WalletTransaction.TransactionType;
import com.exercise.loyalty.repository.TransactionRepository;
import com.exercise.loyalty.repository.WalletRepository;
import com.exercise.loyalty.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class LoyaltyProgramServiceImpl {

    private final TransactionRepository transactionRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletRepository walletRepository;
    private final PointsCalculator pointsCalculator;

    @Autowired
    public LoyaltyProgramServiceImpl(
            TransactionRepository transactionRepository,
            WalletTransactionRepository walletTransactionRepository,
            WalletRepository walletRepository,
            PointsCalculator pointsCalculator) {

        this.transactionRepository = transactionRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletRepository = walletRepository;
        this.pointsCalculator = pointsCalculator;
    }

    public void addTransaction(Transaction transaction)
    {
        transactionRepository.save(transaction);
        WalletTransaction walletTransaction;
        switch (transaction.getFundSource())
        {
            case CASH:
                BigDecimal pendingPoints = pointsCalculator.calculatePendingPoints(transaction.getValue());
                walletTransaction = createWalletTransaction(transaction, pendingPoints);
                walletTransaction.setPointsType(PointsType.PENDING);
                walletTransaction.setTransactionType(TransactionType.CREDIT);
                break;
            case WALLET:
                //TODO Add validation for available points
                BigDecimal availablePointsSpent = pointsCalculator.calculateAvailablePointsSpent(transaction.getValue());
                walletTransaction = createWalletTransaction(transaction, availablePointsSpent);
                walletTransaction.setPointsType(PointsType.AVAILABLE);
                walletTransaction.setTransactionType(TransactionType.DEBIT);
                break;
            default:
                throw new RuntimeException("Fund source not supported: " + transaction.getFundSource());
        }


        walletTransactionRepository.save(walletTransaction);
    }

    private WalletTransaction createWalletTransaction(Transaction transaction, BigDecimal points) {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setCustomerId(transaction.getCustomerId());
        walletTransaction.setPointsAmount(points);
        walletTransaction.setTransactionId(transaction.getId());
        walletTransaction.setTimestamp(new Date());
        return walletTransaction;
    }
}
