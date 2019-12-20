package com.exercise.loyalty.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	private Long id;
	private BigDecimal value;
	
	public Transaction() {
	}

	public Transaction(Long id, BigDecimal value) {
		this.id = id;
		this.value = value;
	}

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
}
