package com.exercise.loyalty.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Wallet {

    @Id
    @GeneratedValue
    private Long id;
    private String customerId;

    private BigDecimal pendingPoints;
    private BigDecimal availablePoints;

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

    public BigDecimal getPendingPoints() {
        return pendingPoints;
    }

    public void setPendingPoints(BigDecimal pendingPoints) {
        this.pendingPoints = pendingPoints;
    }

    public BigDecimal getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(BigDecimal availablePoints) {
        this.availablePoints = availablePoints;
    }
}
