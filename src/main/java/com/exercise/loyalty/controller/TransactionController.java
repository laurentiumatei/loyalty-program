package com.exercise.loyalty.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.loyalty.model.Transaction;
import com.exercise.loyalty.repository.TransactionRepository;

@RestController
@RequestMapping("/api/")
public class TransactionController {

	@Autowired
	private TransactionRepository transactionRepository;
	
	private Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	@RequestMapping(value = "transactions", method = RequestMethod.GET)
	public List<Transaction> list()
	{
		return transactionRepository.findAll();		
	}
}
