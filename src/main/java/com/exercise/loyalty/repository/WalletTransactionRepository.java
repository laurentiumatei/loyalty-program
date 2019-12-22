package com.exercise.loyalty.repository;

import com.exercise.loyalty.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findAllByCustomerIdOrderByTimestampDesc(String customerId);
}
