package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyRoomManager implements Rooms {
  class QueueNode {
    QueueNode next;
    boolean canContinue;
  }


  private final int rooms;
  private final int[] threadsInRoom;
  private final Runnable[] exitHandlers;
  private int activeRoomId;
  private final Lock mutex;
  private final Condition roomsAreEmpty;
  private final ThreadLocal<Integer> threadRoom;
  private final List<Thread> waitingList;
  private final Map<Long, Integer> roomThreadWantsToEnter; // THREAD_ID -> ROOM_ID

  static final int NO_ROOM = -1;

  public MyRoomManager(int rooms) {
    this.rooms = rooms;
    this.threadsInRoom = new int[rooms];
    this.exitHandlers = new Runnable[rooms];
    this.mutex = new ReentrantLock();
    this.roomsAreEmpty = this.mutex.newCondition();
    this.activeRoomId = NO_ROOM;
    this.threadRoom = ThreadLocal.withInitial(() -> NO_ROOM);
    this.waitingList = new LinkedList<>();
    this.roomThreadWantsToEnter = new HashMap<>();
  }

  public void enter(int roomId)
    throws InterruptedException {
    Thread currentThread = Thread.currentThread();
    long threadId = Thread.currentThread().getId();
    this.mutex.lock();
    try {
      roomThreadWantsToEnter.put(threadId, roomId);
      while (hayOtroCuartoOcupado(roomId) ||

             // si alguien llego antes que mi
             // y "quiere" entrar a otro cuarto (!= roomId)
             ) {
        if (!waitingList.contains(currentThread)) {
          waitingList.add(currentThread);
        }
        roomsAreEmpty.await();
      }
      waitingList.remove(currentThread);
      threadRoom.set(roomId);
      threadsInRoom[roomId]++;
      activeRoomId = roomId;
    } finally {
      this.mutex.unlock();
    }
  }

  private boolean hayOtroCuartoOcupado(int roomId) {
    return (activeRoomId != NO_ROOM &&
             roomId != activeRoomId);
  }



  public boolean exit() {
    this.mutex.lock();
    try {
      int myRoom = threadRoom.get();
      threadsInRoom[myRoom]--;
      threadRoom.set(NO_ROOM);
      if (threadsInRoom[myRoom] == 0) {
        activeRoomId = NO_ROOM;
        // cierro la puerta
        exitHandlers[myRoom].run();
        roomsAreEmpty.signalAll();
        return true;
      }
      return false;
    } finally {
      this.mutex.unlock();
    }

  }

  public void setExitHandlers(int roomId, Runnable exitHandler) {

  }

  public int countThreadsInRoom(int roomId) {

  }

  int[] getThreadsInRooms() {

  }
}
