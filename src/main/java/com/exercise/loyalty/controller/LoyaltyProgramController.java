package com.exercise.loyalty.controller;

import java.util.List;

import com.exercise.loyalty.model.WalletTransaction;
import com.exercise.loyalty.repository.WalletTransactionRepository;
import com.exercise.loyalty.service.LoyaltyProgramServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.repository.TransactionRepository;

@RestController
@RequestMapping("/api/")
public class LoyaltyProgramController {

	private TransactionRepository transactionRepository;
	private LoyaltyProgramServiceImpl loyaltyProgramService;
	private WalletTransactionRepository walletTransactionRepository;
	
	private Logger logger = LoggerFactory.getLogger(LoyaltyProgramController.class);

	@Autowired
	public LoyaltyProgramController(
			TransactionRepository transactionRepository,
			LoyaltyProgramServiceImpl loyaltyProgramService,
			WalletTransactionRepository walletTransactionRepository) {
		this.transactionRepository = transactionRepository;
		this.loyaltyProgramService = loyaltyProgramService;
		this.walletTransactionRepository = walletTransactionRepository;
	}

	@RequestMapping(value = "transactions", method = RequestMethod.GET)
	public List<Transaction> list()
	{
		return transactionRepository.findAll();
	}

	@RequestMapping(value = "transactions", method = RequestMethod.POST)
	public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction)
	{
		loyaltyProgramService.addTransaction(transaction);
		return ResponseEntity.ok().build();

	}

	@RequestMapping(value = "history", method = RequestMethod.GET)
	public List<WalletTransaction> history()
	{
		return walletTransactionRepository.findAll();
	}
}
