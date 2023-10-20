package unam.ciencias.computoconcurrente.blockingsynchronization;

import unam.ciencias.computoconcurrente.soexamples.ThreadID;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RoomManager implements Rooms {
  private final int availableRooms;
  private final int[] threadsInRoom;
  private final Runnable[] exitHandlers;

  // threadId -> roomId mapping
  private final Map<Integer,Integer> threadRoom;
  // threadId -> turn mapping
  private final Map<Integer,Long> threadTurn;
  private int busyRoomId;
  // threadId -> roomId mapping
  private final Map<Integer,Integer> roomThreadWantsToEnter;
  private long nextTurn;

  private final Lock mutex;
  private final Condition roomsAreEmpty;

  static final int NO_ROOM = -1;

  public RoomManager(int rooms) {
    this.availableRooms = rooms;
    this.threadsInRoom = new int[rooms];
    this.threadRoom = new HashMap<>();
    this.exitHandlers = new Runnable[rooms];
    this.mutex = new ReentrantLock();
    this.roomsAreEmpty = this.mutex.newCondition();
    this.busyRoomId = NO_ROOM;
    this.nextTurn = 0;
    this.threadTurn = new HashMap<>();
    this.roomThreadWantsToEnter = new HashMap<>();
  }

  @Override
  public void enter(int roomId) throws InterruptedException {
    this.mutex.lock();
    try {
      int myThreadId = ThreadID.get();
      // Using a similar solution to Lamport's Bakery Algorithm
      // raise current thread's flag
      this.roomThreadWantsToEnter.put(myThreadId, roomId);
      // get next turn
      long myTurn = this.getNextTurn();
      this.threadTurn.put(myThreadId, myTurn);

      // Wait if
      //  - current busy room is not the one the current thread wants to enter
      //  - current busy room is the one the current thread wants to enter,
      //    but there are other threads waiting to enter other rooms with a lower
      //    turn than current thread's
      while (
        this.isNotTheBusyRoom(roomId) ||
        this.isBusyRoomButAnyoneGoesBefore(myTurn, roomId)
      ) {
        roomsAreEmpty.await();
      }
      // current thread already entered the room,
      // so we remove the thread's waiting state data
      this.roomThreadWantsToEnter.remove(myThreadId);
      this.threadTurn.remove(myThreadId);

      this.threadsInRoom[roomId]++;
      this.threadRoom.put(myThreadId, roomId);
      this.busyRoomId = roomId;
    } finally {
      this.mutex.unlock();
    }
  }

  private long getNextTurn() {
    return this.nextTurn++;
  }

  private boolean isNotTheBusyRoom(int roomId) {
    return (this.isThereABusyRoom() && busyRoomId != roomId);
  }

  private boolean isBusyRoomButAnyoneGoesBefore(long myTurn, int roomId) {
    return (isBusyRoom(roomId) && anyoneMustEnterBefore(myTurn, roomId));
  }

  private boolean isBusyRoom(int roomId) {
    return (this.isThereABusyRoom() && busyRoomId == roomId);
  }

  private boolean isThereABusyRoom() {
    return busyRoomId != NO_ROOM;
  }

  private boolean anyoneMustEnterBefore(long myTurn, int roomId) {
    // There exist other thread with a lower turn, waiting to enter other room
    return this.threadTurn.entrySet().stream()
      .anyMatch(entry -> {
        int otherThreadId = entry.getKey();
        long otherThreadTurn = entry.getValue();
        return otherThreadTurn < myTurn &&
          this.roomThreadWantsToEnter.get(otherThreadId) != roomId;
      });
  }

  @Override
  public boolean exit() {
    this.mutex.lock();
    try {
      int roomId = this.threadRoom.get(ThreadID.get());
      this.threadsInRoom[roomId]--;
      if (this.isLastThreadToLeaveRoom(roomId)) {
        busyRoomId = NO_ROOM;
        this.exitHandlers[roomId].run();
        roomsAreEmpty.signalAll();
        return true;
      }
      return false;
    } finally {
      this.mutex.unlock();
    }
  }

  private boolean isLastThreadToLeaveRoom(int roomId) {
    return this.threadsInRoom[roomId] == 0;
  }

  @Override
  public void setExitHandlers(int roomId, Runnable exitHandler) {
    this.exitHandlers[roomId] = exitHandler;
  }

  @Override
  public int countThreadsInRoom(int roomId) {
    this.mutex.lock();
    try {
      return this.threadsInRoom[roomId];
    } finally {
      this.mutex.unlock();
    }
  }

  @Override
  public int[] getThreadsInRooms() {
    this.mutex.lock();
    try {
      return this.threadsInRoom.clone();
    } finally {
      this.mutex.unlock();
    }
  }
}
