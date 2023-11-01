package com.woony;

import com.woony.Philosopher.Chopstick;

public class PhilosopherProblem {
    public static void main(String[] args) throws InterruptedException {

        Chopstick left1 = new Chopstick();
        Chopstick right1 = new Chopstick();
        Chopstick left2 = new Chopstick();
        Chopstick right2 = new Chopstick();
        Chopstick left3 = new Chopstick();
        Chopstick right3 = new Chopstick();
        Chopstick left4 = new Chopstick();
        Chopstick right4 = new Chopstick();
        Chopstick left5 = new Chopstick();
        Chopstick right5 = new Chopstick();

        Philosopher ph1 = new Philosopher("철학자 1", left1, right5);
        Philosopher ph2 = new Philosopher("철학자 2", left2, right1);
        Philosopher ph3 = new Philosopher("철학자 3", left3, right2);
        Philosopher ph4 = new Philosopher("철학자 4", left4, right3);
        Philosopher ph5 = new Philosopher("철학자 5", left5, right4);


        ph1.start();
        ph2.start();
        ph3.start();
        ph4.start();
        ph5.start();

    }
}
