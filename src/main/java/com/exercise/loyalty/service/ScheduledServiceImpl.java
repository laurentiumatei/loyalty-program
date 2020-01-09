package com.exercise.loyalty.service;

import static com.exercise.loyalty.helper.DateTimeHelper.getStartOfDayDaysAgo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exercise.loyalty.model.Wallet;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.repository.WalletRepository;
import com.exercise.loyalty.repository.WalletTransactionRepository;

@Service
@Transactional
public class ScheduledServiceImpl implements ScheduledService {

    private static final BigDecimal MINIMUM_PENDING_POINTS = BigDecimal.valueOf(500);
    private static final int DAYS_AGO_TO_SEARCH_TRANSACTIONS = 6;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private Logger logger = LoggerFactory.getLogger(ScheduledServiceImpl.class);

    @Autowired
    public ScheduledServiceImpl(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }
    
    @Override
	@Scheduled(cron = "0 0 23 * * SUN") // Should run every Sunday at 23:00, although I did not manage to make in run automatically on my PC. TODO Find the correct cron expression
    public void allocateAvailablePoints() {
        logger.info("Starting allocation of available points...");
        List<Wallet> wallets = walletRepository.findAll();
        wallets.stream().filter(w -> w.getPendingPoints().compareTo(BigDecimal.ZERO) > 0)
                .forEach(this::allocateAvailablePoints);
        logger.info("Finished allocation of available points.");
    }

    private void allocateAvailablePoints(Wallet wallet) {
        logger.info("Checking customerId: " + wallet.getCustomerId());

        boolean atLeastOneTransactionExistsOnEveryDay =
                atLeastOneTransactionExistsOnEveryDayOfTheWeek(wallet.getCustomerId());
        boolean isMinimumAmountSpent = isMinimumAmountSpentThisWeek(wallet.getCustomerId());
        logger.info("Is minimum amount spent this week: " + isMinimumAmountSpent);
        logger.info("At least one transaction exists on every day of the week: " + atLeastOneTransactionExistsOnEveryDay);

        if (atLeastOneTransactionExistsOnEveryDay && isMinimumAmountSpent) {
            logger.info("Allocating " + wallet.getPendingPoints() + " available points");
            wallet.setAvailablePoints(wallet.getAvailablePoints().add(wallet.getPendingPoints()));
            wallet.setPendingPoints(BigDecimal.ZERO);
        }
        walletRepository.save(wallet);
    }

    private boolean atLeastOneTransactionExistsOnEveryDayOfTheWeek(String customerId) {
        for (int i = DAYS_AGO_TO_SEARCH_TRANSACTIONS; i >= 0; i--) {
            Date startOfDay = getStartOfDayDaysAgo(i);
            Date startOfNextDay = getStartOfDayDaysAgo(i - 1);
            List<WalletTransaction> walletTransactions = getWalletTransactionsBetween(customerId, startOfDay, startOfNextDay);
            if (walletTransactions.isEmpty()) {
                logger.info("No transactions found between: " + startOfDay + " and: " + startOfNextDay);
                return false;
            }
        }
        return true;
    }

    private boolean isMinimumAmountSpentThisWeek(String customerId) {
        Date beginningOfWeekDate = getStartOfDayDaysAgo(DAYS_AGO_TO_SEARCH_TRANSACTIONS);

        List<WalletTransaction> walletTransactions = getWalletTransactionsBetween(customerId, beginningOfWeekDate, new Date());
        BigDecimal sumOfTransactions = walletTransactions.stream()
                .map(WalletTransaction::getPointsAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("CustomerId " + customerId + "  accumulated " + sumOfTransactions + " pending points this week.");
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
}
