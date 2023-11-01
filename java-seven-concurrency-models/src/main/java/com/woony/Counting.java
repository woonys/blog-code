package com.woony;

public class Counting {
    public static void main(String[] args) throws InterruptedException {
        class Counter {
            private int count = 0;

            public synchronized void increment() { // 얘가 호출되면 Counter 객체의 잠금장치를 요구. 메서드 리턴할 때 잠금장치도 해제된다.
                ++count;
            }

            public int getCount() {
                return count;
            }
        }
        final Counter counter = new Counter();
        class CountingThread extends Thread {
            private String name;
            CountingThread(String name) {
                this.name = name;
            }
            public void run() {
                for (int x = 0; x < 10000; ++x) {
                    counter.increment();
                    System.out.println(name + "이 1 더했습니다.");
                }
            }
        }
            CountingThread t1 = new CountingThread("t1");
            CountingThread t2 = new CountingThread("t2");
            t1.start();
            t2.start();
            t1.join();
            t2.join();

        System.out.println(counter.getCount());
    }
}
