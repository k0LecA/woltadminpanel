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
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime dateCreated;
    @ManyToOne
    @JoinColumn(name ="chat_id")
    private Chat chat;

    public Message(String message, User user, Chat chat) {
        this.message = message;
        this.user = user;
        this.dateCreated = LocalDateTime.now();
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "[" + dateCreated + "] " + user.getName() + ": " + message;
    }

}
