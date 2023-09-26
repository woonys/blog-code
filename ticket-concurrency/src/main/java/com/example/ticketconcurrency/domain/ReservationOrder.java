package com.example.ticketconcurrency.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Component
@Getter
@NoArgsConstructor
public class ReservationOrder {
    private int totalAmount;

    public ReservationOrder(int totalAmount) {
        this.totalAmount = totalAmount;
    }


    public boolean isPossibleToReserve(Ticket ticket) {
        return ticket.getId() <= totalAmount;
    }
}
