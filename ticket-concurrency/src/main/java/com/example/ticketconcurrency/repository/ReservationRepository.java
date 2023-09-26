package com.example.ticketconcurrency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketconcurrency.domain.Reservation;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
