package com.example.ticketconcurrency.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketconcurrency.domain.Reservation;
import com.example.ticketconcurrency.domain.ReservationOrder;
import com.example.ticketconcurrency.domain.Ticket;
import com.example.ticketconcurrency.repository.ReservationRepository;
import com.example.ticketconcurrency.repository.TicketRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class TicketingService {
    private ReservationOrder reservationOrder;
    private TicketRepository ticketRepository;
    private ReservationRepository reservationRepository;

    @Transactional
    public void ticketing() {
        Ticket ticket = createTicket();
        handleReservation(ticket);
        ticketRepository.save(ticket);
    }

    private void handleReservation(Ticket ticket) {
        if (reservationOrder.isPossibleToReserve(ticket)) {
            ticket.succeed();
            reservationRepository.save(new Reservation(ticket.getId()));
        } else {
            ticket.failed();
        }
    }

    public Ticket createTicket() {
        return ticketRepository.save(new Ticket());
    }
}
