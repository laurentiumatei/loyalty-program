package com.exercise.loyalty.controller;

import java.util.List;

import com.exercise.loyalty.model.Wallet;
import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.service.TransactionService;
import com.exercise.loyalty.service.WalletService;
import com.exercise.loyalty.service.WalletTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.repository.TransactionRepository;

@RestController
@RequestMapping("/api/")
public class LoyaltyProgramController {

	private final TransactionService loyaltyProgramService;
	private final WalletTransactionService walletTransactionRepository;
	private final WalletService walletService;
	
	private Logger logger = LoggerFactory.getLogger(LoyaltyProgramController.class);

	@Autowired
	public LoyaltyProgramController(
			TransactionService loyaltyProgramService,
			WalletTransactionService walletTransactionService,
			WalletService walletService) {
		this.loyaltyProgramService = loyaltyProgramService;
		this.walletTransactionRepository = walletTransactionService;
		this.walletService = walletService;
	}

	@RequestMapping(value = "transactions", method = RequestMethod.POST)
	public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction)
	{
		loyaltyProgramService.addTransaction(transaction);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "history/{customerId}", method = RequestMethod.GET)
	public List<WalletTransaction> history(@PathVariable String customerId)
	{
		return walletTransactionRepository.getTransactions(customerId);
	}

	@RequestMapping(value = "balance/{customerId}", method = RequestMethod.GET)
	public Wallet balance(@PathVariable String customerId)
	{
		return walletService.getWallet(customerId);
	}
}
