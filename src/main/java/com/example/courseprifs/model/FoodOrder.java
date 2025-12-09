package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> itemList;
    private Double price;
    private Double deliveryFee;
    @ManyToOne
    @JoinColumn(name = "delivery_address_id", nullable = true)
    private Address deliveryAddress;
    @ManyToOne
    private BasicUser buyer;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="chat_id")
    private Chat chat;
    @ManyToOne
    private Restaurant restaurant;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne
    private Driver driver;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public FoodOrder(String name,Double deliveryFee, BasicUser buyer, Restaurant restaurant,Address deliveryAddress, OrderStatus orderStatus) {
        this.name = name;
        this.deliveryFee = deliveryFee ;
        this.buyer = buyer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.orderStatus = orderStatus;
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();

    }

    @Override
    public String toString() {
        return name + " " + price;
    }
}
