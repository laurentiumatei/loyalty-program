package com.exercise.loyalty.repository;

import com.exercise.loyalty.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findAllByCustomerIdOrderByTimestampDesc(String customerId);

    List<WalletTransaction> findAllByCustomerIdAndPointsTypeAndTransactionTypeOrderByTimestampDesc(
            String customerId,
            WalletTransaction.PointsType pointsType,
            WalletTransaction.TransactionType transactionType
    );

    List<WalletTransaction> findAllByCustomerIdAndPointsTypeAndTransactionTypeAndTimestampBetween(
            String customerId,
            WalletTransaction.PointsType pointsType,
            WalletTransaction.TransactionType transactionType,
            Date start,
            Date end
    );
}
