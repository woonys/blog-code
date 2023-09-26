package com.example.ticketconcurrency.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketconcurrency.domain.Reservation;
import com.example.ticketconcurrency.domain.TicketReservationAmount;
import com.example.ticketconcurrency.domain.Ticket;
import com.example.ticketconcurrency.repository.ReservationRepository;
import com.example.ticketconcurrency.repository.TicketRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketingService {
    private TicketReservationAmount reservation;
    private TicketRepository ticketRepository;
    private ReservationRepository reservationRepository;

    @Transactional
    public void ticketing() {
        Ticket ticket = createTicket();
        if (reservation.isPossibleToReserve(ticket)) {
            reservation.increaseReservedAmount();
            ticket.succeed();
            ticketRepository.save(ticket);
            reservationRepository.save(new Reservation(ticket.getId()));
            return;
        }
        ticket.failed();
        throw new IllegalStateException("예약이 불가능합니다.");
    }

    public Ticket createTicket() {
        Ticket ticket = new Ticket();
        return ticketRepository.save(ticket);
    }
}
