package com.exercise.loyalty.helper;

import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.model.WalletTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class WalletTransactionHelper {

    public static WalletTransaction createWalletTransaction(Transaction transaction, BigDecimal points) {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setCustomerId(transaction.getCustomerId());
        walletTransaction.setPointsAmount(points);
        walletTransaction.setTransactionId(transaction.getId());
        walletTransaction.setTimestamp(new Date());
        return walletTransaction;
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
