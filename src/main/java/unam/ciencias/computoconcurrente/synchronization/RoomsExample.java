package unam.ciencias.computoconcurrente.synchronization;

import unam.ciencias.computoconcurrente.soexamples.ThreadID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RoomsExample {
  static final int THREADS = 8;
  static final int ROOMS = 3;
  static final int ITERATIONS = 1000;
  static final int SIMULATION_DURATION_MS = 10000;
  static List<Thread> allThreads = new ArrayList<>(THREADS);
  static final Rooms rooms;
  static int validStates = 0;
  static int totalStates = 0;

  static Boolean[] threadFinished = new Boolean[THREADS];

  static {
    rooms =  new RoomsImpl(ROOMS);
    for (int roomId = 0; roomId < ROOMS; roomId++) {
      final int finalRoomId = roomId;
      rooms.setExitHandlers(roomId, () -> System.out.println("last thread to leave room " + finalRoomId));
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread verifier = new Thread(RoomsExample::verifyRoomsState, "Verifier");
    verifier.setDaemon(true);

    for (int i = 0; i < THREADS; i++) {
      Thread consumer = new Thread(RoomsExample::doWork, "Thread" + (i+1));
      allThreads.add(consumer);
    }

    // start all threads
    verifier.start();
    for (var thread : allThreads) {
      thread.start();
    }

    int waits = 0;
    while(allThreads.stream().anyMatch(Thread::isAlive) && waits * 1000 < SIMULATION_DURATION_MS) {
      Thread.sleep(1000);
      waits++;
    }

    for (var thread : allThreads) {
      if (thread.isAlive()) {
        thread.interrupt();
      }
    }
    verifier.join();

    System.out.printf("Valid states vs total states %d vs %d\n", validStates, totalStates);
    System.out.printf("All threads finished %s\n", Arrays.stream(threadFinished).allMatch(finished -> finished));
  }

  static void doWork() {
    threadFinished[ThreadID.get()] = false;
    for (int it = 0; it < ITERATIONS; it++) {
      if (Thread.interrupted()) {
        System.out.printf("Thread %s was interrupted after %d rounds\n",
          Thread.currentThread().getName(),
          it
        );
        return;
      }
      int roomId = Math.abs(ThreadLocalRandom.current().nextInt() % ROOMS);
      rooms.enter(roomId);
      if (!sleepRandomTime(10)) {
        continue;
      }
      rooms.exit();
    }
    threadFinished[ThreadID.get()] = true;
  }

  static void verifyRoomsState() {
    while (allThreads.stream().anyMatch(Thread::isAlive)) {
      int[] threadsInRoom = rooms.getThreadsInRooms();
      int busyRooms = 0;
      for (int threadsInRoomI : threadsInRoom) {
        busyRooms += threadsInRoomI > 0 ? 1 : 0;
      }
      if (busyRooms  <= 1) {
        validStates++;
      }
      totalStates++;
      sleepRandomTime(5);
    }
  }

  static boolean sleepRandomTime(int limit) {
    try {
      if (!Thread.currentThread().getName().equals("main")) {
        Thread.sleep(Math.abs(ThreadLocalRandom.current().nextInt() % limit));
      }
      return true;
    } catch (InterruptedException e) {
      return false;
    }
  }
}
