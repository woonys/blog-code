package com.example.ticketconcurrency.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ReservationStatus reservationStatus;

    public Ticket() {
        this.reservationStatus = ReservationStatus.WAITING;
    }

    public void succeed() {
        this.reservationStatus = ReservationStatus.RESERVED;
    }

    public void failed() {
        this.reservationStatus = ReservationStatus.FAILED;
    }
}
