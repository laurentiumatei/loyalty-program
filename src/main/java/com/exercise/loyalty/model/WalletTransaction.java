package com.exercise.loyalty.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class WalletTransaction {
    @Id
    @GeneratedValue
    private Long id;
    private String customerId;
    private BigDecimal pointsAmount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private PointsType pointsType;
    private Long transactionId;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="UTC")
    private Date timestamp;

    public WalletTransaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getPointsAmount() {
        return pointsAmount;
    }

    public void setPointsAmount(BigDecimal pointsAmount) {
        this.pointsAmount = pointsAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public PointsType getPointsType() {
        return pointsType;
    }

    public void setPointsType(PointsType pointsType) {
        this.pointsType = pointsType;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum PointsType {
        PENDING, AVAILABLE
    }
}
