package com.example.courseprifs.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name ="order_id")
    FoodOrder order;
    @ManyToOne
    @JoinColumn(name="cuisine_id")
    private Cuisine cuisine;
    private int quantity;
    private double price;
    private String specialRequest;

    public OrderItem(FoodOrder foodOrder, Cuisine cuisine, int quantity, double price, String specialRequest) {
        this.order = foodOrder;
        this.cuisine = cuisine;
        this.quantity = quantity;
        this.price = price;
        this.specialRequest = specialRequest;
    }

    @Override
    public String toString() { return cuisine.toString()+" "+quantity+" "+price+" "+specialRequest;}
}
