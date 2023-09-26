package com.example.ticketconcurrency.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketconcurrency.domain.ReservationStatus;
import com.example.ticketconcurrency.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long id);
    List<Ticket> findByReservationStatus(ReservationStatus status);
}
