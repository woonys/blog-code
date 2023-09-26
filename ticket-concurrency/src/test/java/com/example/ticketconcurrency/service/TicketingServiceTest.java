package com.example.ticketconcurrency.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ticketconcurrency.domain.TicketReservationAmount;
import com.example.ticketconcurrency.repository.ReservationRepository;
import com.example.ticketconcurrency.repository.TicketRepository;

@SpringBootTest
class TicketingServiceTest {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void test() throws InterruptedException {
        //given
        int memberCount = 30;
        int ticketAmount = 10;

        final TicketReservationAmount ticketReservationAmount = new TicketReservationAmount(ticketAmount);
        TicketingService ticketingService = new TicketingService(ticketReservationAmount, ticketRepository, reservationRepository);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        //ExecutorService executorsService = Executors.newFixedThreadPool(memberCount);
        //CountDownLatch latch = new CountDownLatch(memberCount);

        for (int i = 0; i < memberCount; i++) {
            try {
                ticketingService.ticketing();
                successCount.incrementAndGet();
            } catch (Exception e) {
                System.out.println(e);
                failCount.incrementAndGet();
            }
        }

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);
    }

}