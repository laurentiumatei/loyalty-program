package com.exercise.loyalty.service;

import com.exercise.loyalty.model.Wallet;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.repository.WalletRepository;
import com.exercise.loyalty.repository.WalletTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ScheduledService {

    private static final BigDecimal MINIMUM_PENDING_POINTS = BigDecimal.valueOf(500);
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private Logger logger = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    public ScheduledService(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    /*should run every Sunday at 23:00, although I did not manage to make in run automatically on my PC
    TODO Find the correct cron expression
    */
    @Scheduled(cron = "0 0 23 * * SUN")
    public void allocateAvailablePoints() {
        logger.info("Trying to allocate pending points to available points...");
        List<Wallet> wallets = walletRepository.findAll();
        wallets.stream().filter(w -> w.getPendingPoints().compareTo(BigDecimal.ZERO) > 0)
                .forEach(this::allocateAvailablePoints);
    }

    private void allocateAvailablePoints(Wallet wallet) {
        boolean atLeastOneTransactionExistsOnEveryDayOfTheWeek =
                atLeastOneTransactionExistsOnEveryDayOfTheWeek(wallet.getCustomerId());
        boolean isMinimumAmountSpentThisWeek = isMinimumAmountSpentThisWeek(wallet.getCustomerId());

        if (atLeastOneTransactionExistsOnEveryDayOfTheWeek && isMinimumAmountSpentThisWeek) {
            wallet.setAvailablePoints(wallet.getAvailablePoints().add(wallet.getPendingPoints()));
            wallet.setPendingPoints(BigDecimal.ZERO);
        }
        walletRepository.save(wallet);
    }

    private boolean atLeastOneTransactionExistsOnEveryDayOfTheWeek(String customerId) {
        for (int i = 1; i <= 7; i++) {

            Date startOfDay = getDateInDayOfWeek(i);
            Date startOfNextDay = getDateStartOfNextDayOfWeek(i);
            List<WalletTransaction> walletTransactions = getWalletTransactionsBetween(customerId, startOfDay, startOfNextDay);
            if (walletTransactions.isEmpty()) {
                logger.info("No transactions found on date: " + startOfDay);
                return false;
            }
        }
        return true;
    }

    private boolean isMinimumAmountSpentThisWeek(String customerId) {
        Date beginningOfWeekDate = getDateInDayOfWeek(1);

        List<WalletTransaction> walletTransactions = getWalletTransactionsBetween(customerId, beginningOfWeekDate, new Date());
        BigDecimal sumOfTransactions = walletTransactions.stream()
                .map(WalletTransaction::getPointsAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("Pending points accumulated this week: " + sumOfTransactions);
        return sumOfTransactions.compareTo(MINIMUM_PENDING_POINTS) >= 0;
    }

    private List<WalletTransaction> getWalletTransactionsBetween(String customerId, Date startDate, Date endDate) {
        return walletTransactionRepository.findAllByCustomerIdAndPointsTypeAndTransactionTypeAndTimestampBetween(
                customerId,
                WalletTransaction.PointsType.PENDING,
                WalletTransaction.TransactionType.CREDIT,
                startDate,
                endDate);
    }

    private Date getDateInDayOfWeek(int dayOfWeek) {
        LocalDateTime beginningOfDay = LocalDate.now().with(ChronoField.DAY_OF_WEEK, dayOfWeek).atStartOfDay();
        return Date.from(beginningOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date getDateStartOfNextDayOfWeek(int dayOfWeek) {
        LocalDateTime beginningOfNextDay = LocalDate.now().with(ChronoField.DAY_OF_WEEK, dayOfWeek).atStartOfDay().plusDays(1);
        return Date.from(beginningOfNextDay.atZone(ZoneId.systemDefault()).toInstant());
    }
}
