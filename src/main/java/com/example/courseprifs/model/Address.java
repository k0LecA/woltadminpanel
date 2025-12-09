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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;



    private String street;
    private String city;
    private String postalCode;
    private String instructions;

    public Address(String street, String city, String postalCode, String instructions) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.instructions = instructions;
    }

    @Override
    public String toString() { return street + ", " + city + ", " + postalCode; }
}
