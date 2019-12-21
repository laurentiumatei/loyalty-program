package com.exercise.loyalty.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	private Long id;
	private String customerId;
	private BigDecimal value;
	
	@Enumerated(EnumType.STRING)
	private FundSource fundSource;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public FundSource getFundSource() {
		return fundSource;
	}

	public void setFundSource(FundSource fundSource) {
		this.fundSource = fundSource;
	}

	public enum FundSource {
		CASH, WALLET
	}
}
