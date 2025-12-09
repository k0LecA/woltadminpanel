package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double amount;
    private String transactionId;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name="order_id")
    private FoodOrder order;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment(String transactionId,Double amount,FoodOrder order,PaymentStatus status){
        this.transactionId=transactionId;
        this.amount=amount;
        this.order=order;
        this.status=status;
        this.timestamp=LocalDateTime.now();
    }

    @Override
    public String toString() {return order.getName()+" "+amount+" "+timestamp+" "+status;}
}
