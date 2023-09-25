package com.example.ticketconcurrency.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Reservation {
    @Id
    private Long id;
    private int totalAmount;
    private int reservedAmount;
}
