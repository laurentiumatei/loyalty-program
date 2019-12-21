package com.exercise.loyalty.service;

import com.exercise.loyalty.component.PointsCalculator;
import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.model.WalletTransaction.PointsType;
import com.exercise.loyalty.model.WalletTransaction.TransactionType;
import com.exercise.loyalty.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletTransactionService walletTransactionService;
    private final PointsCalculator pointsCalculator;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            WalletTransactionService walletTransactionService,
            PointsCalculator pointsCalculator) {

        this.transactionRepository = transactionRepository;
        this.walletTransactionService = walletTransactionService;
        this.pointsCalculator = pointsCalculator;
    }

    @Override
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
                BigDecimal availablePointsSpent = pointsCalculator.calculateAvailablePointsSpent(transaction.getValue());
                walletTransaction = createWalletTransaction(transaction, availablePointsSpent);
                walletTransaction.setPointsType(PointsType.AVAILABLE);
                walletTransaction.setTransactionType(TransactionType.DEBIT);
                break;
            default:
                throw new RuntimeException("Fund source not supported: " + transaction.getFundSource());
        }

        walletTransactionService.addWalletTransaction(walletTransaction);
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
