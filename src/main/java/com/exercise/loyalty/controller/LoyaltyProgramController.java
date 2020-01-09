package com.exercise.loyalty.controller;

import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.model.Wallet;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.service.ScheduledService;
import com.exercise.loyalty.service.TransactionService;
import com.exercise.loyalty.service.WalletService;
import com.exercise.loyalty.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class LoyaltyProgramController {

    private final TransactionService loyaltyProgramService;
    private final WalletTransactionService walletTransactionRepository;
    private final WalletService walletService;
    private final ScheduledService scheduledService;

    @Autowired
    public LoyaltyProgramController(
            TransactionService loyaltyProgramService,
            WalletTransactionService walletTransactionService,
            WalletService walletService,
            ScheduledService scheduledService) {
        this.loyaltyProgramService = loyaltyProgramService;
        this.walletTransactionRepository = walletTransactionService;
        this.walletService = walletService;
        this.scheduledService = scheduledService;
    }

    @RequestMapping(value = "transaction", method = RequestMethod.POST)
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        if (transaction.getValue().compareTo(BigDecimal.ZERO) <= 0)
        {
            return ResponseEntity.badRequest().body("Value of the transaction must be grater than 0.");
        }

        loyaltyProgramService.addTransaction(transaction);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "history/{customerId}", method = RequestMethod.GET)
    public List<WalletTransaction> history(@PathVariable String customerId) {
        return walletTransactionRepository.getWalletTransactions(customerId);
    }

    @RequestMapping(value = "balance/{customerId}", method = RequestMethod.GET)
    public Wallet balance(@PathVariable String customerId) {
        return walletService.getWallet(customerId);
    }

    @RequestMapping(value = "allocateAvailablePoints", method = RequestMethod.GET)
    public void allocateAvailablePoints() {
        scheduledService.allocateAvailablePoints();
    }
}
