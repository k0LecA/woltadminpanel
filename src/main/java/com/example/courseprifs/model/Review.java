package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends Message{
    private int rating;
    @ManyToOne
    private BasicUser feedbackUser;

    public Review(String text, User user, int rating, BasicUser feedbackUser) {
        super(text,user,null);
        this.rating = rating;
        this.feedbackUser = feedbackUser;
    }
}
