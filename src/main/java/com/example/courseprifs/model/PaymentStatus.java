package com.example.courseprifs.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

public enum PaymentStatus {
    PENDING,
    FAILED,
    RENFUNDED,
    COMPLETED
}