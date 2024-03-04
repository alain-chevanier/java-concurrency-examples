package unam.ciencias.computoconcurrente.spinlocks;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalExample {
    static AtomicInteger nextTurn = new AtomicInteger();

    static Map<Long, Integer> threadTurn = new Hashtable<>();

    static ThreadLocal<Integer> myTurn = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(ThreadLocalExample::threadWork);
        Thread t2 = new Thread(ThreadLocalExample::threadWork);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Main thread finished");
    }

    static void threadWork() {
        int myTurn = nextTurn.getAndIncrement();
        threadTurn.put(Thread.currentThread().getId(), myTurn);
        ThreadLocalExample.myTurn.set(myTurn);
        printMyId();
        threadPrintGoodByeMessage();
    }

    static void printMyId() {
        System.out.printf("Hello from thread: %d\n", myTurn.get());
    }

    static void threadPrintGoodByeMessage() {
        System.out.printf("thread %d finished", myTurn.get());
    }

}
