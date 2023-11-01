package com.woony;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Philosopher extends Thread {
    private final String name;
    private final Chopstick left, right;
    private Random random;
    private boolean running = true;
    public static class Chopstick {
    }

    public Philosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.left = left;
        this.right = right;
        this.random = new Random();
    }

    public void run() {
        while (running) {
            try {
                think();
                eat();
            } catch (InterruptedException e) {
                System.out.println("인터럽트");
            }
        }
    }

    private void think() throws InterruptedException {
        Thread.sleep(random.nextInt(1000));
        System.out.println(name + "가 식사 전 생각 중입니다.");
    }

    private void eat() throws InterruptedException {
        synchronized (left) {
            System.out.println(name + "가 왼쪽 젓가락을 집었습니다.");
            synchronized (right) {
                System.out.println(name + "가 오른쪽 젓가락을 집었습니다.");
                Thread.sleep(random.nextInt(1000)); // eat
                System.out.println(name + "가 식사를 마쳤습니다.");
            }
        }
    }

    public void stopRunning() {
        this.running = false;
        this.interrupt();
    }

}
