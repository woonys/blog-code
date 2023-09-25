package com.example.ticketconcurrency.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private Long id;
    private Ticket ticket;

    public User(Ticket ticket) {this.ticket = ticket;}
}
