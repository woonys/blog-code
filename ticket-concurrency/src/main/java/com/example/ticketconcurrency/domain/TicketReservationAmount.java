package com.example.ticketconcurrency.domain;

import javax.persistence.Id;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Component
@Getter
@NoArgsConstructor
public class TicketReservationAmount {
    private int totalAmount;
    private int reservedAmount;

    public TicketReservationAmount(int totalAmount) {
        this.totalAmount = totalAmount;
        this.reservedAmount = 0;
    }

    public void increaseReservedAmount() {
        this.reservedAmount++;
    }

    public boolean isPossibleToReserve(Ticket ticket) {
        return ticket.getId() <= totalAmount;
    }

    public void setTotalAmount(int ticketAmount) {
        totalAmount = ticketAmount;
    }
}
