package org.u2soft.billtasticbackend.entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name ="payments")
public class Payment {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Getter
@Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private String status;
    @Column(name="payment_date")
    private LocalDateTime paymentDate;

    public Long getId(){
        return  id;

    }
    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}


