package com.woony;

public class Uninterruptible {
    public static void main(String[] args) throws InterruptedException {
        /*
        해당 프로그램은 데드락 상태를 영원히 유지. 내재된 잠금장치를 얻으려다 블로킹된 스레드는 중간에 중단할 수 없기 때문.
         */
        final Object o1 = new Object();
        final Object o2 = new Object();

        Thread t1 = new Thread() {
            public void run() {
                try {
                    synchronized (o1) {
                        Thread.sleep(1000);
                        synchronized (o2) {}
                    }
                } catch (InterruptedException e) {
                    System.out.println("t1 interrupted");
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                try {
                    synchronized (o2) {
                        Thread.sleep(1000);
                        synchronized (o1) {}
                    }
                } catch (InterruptedException e) {
                    System.out.println("t2 interrupted");
                }
            }
        };

        t1.start();
        t2.start();

        Thread.sleep(2000);
        t1.interrupt();
        t2.interrupt();
        t1.join();
        t2.join();
    }
}
