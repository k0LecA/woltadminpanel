package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("BASIC")
public class BasicUser extends User{
    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //protected List<Address> addresses;
    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    protected List<FoodOrder> myOrders;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    protected List<Message> messages;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> myReviews;
    @OneToMany(mappedBy = "feedbackUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> feedback;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }


    public BasicUser(String login, String password, String name, String surname, String phoneNumber, String address) {
        super(login, password, name, surname, phoneNumber);
        //this.address = address;
        this.myReviews = new ArrayList<>();
        this.feedback = new ArrayList<>();
        this.myOrders = new ArrayList<>();
    }

    @Override
    public String toString() { return name + " " + surname; }
}
