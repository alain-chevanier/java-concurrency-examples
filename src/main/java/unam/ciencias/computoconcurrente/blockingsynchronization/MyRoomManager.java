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
  private final Condition inWaitingList;
  private final ThreadLocal<Integer> threadRoom;
  private final List<Long> waitingList;
  private final Map<Long, Integer> roomThreadWantsToEnter; // THREAD_ID -> ROOM_ID

  static final int NO_ROOM = -1;

  public MyRoomManager(int rooms) {
    this.rooms = rooms;
    this.threadsInRoom = new int[rooms];
    this.exitHandlers = new Runnable[rooms];
    this.mutex = new ReentrantLock();
    this.inWaitingList = this.mutex.newCondition();
    this.activeRoomId = NO_ROOM;
    this.threadRoom = ThreadLocal.withInitial(() -> NO_ROOM);
    this.waitingList = new LinkedList<>();
    this.roomThreadWantsToEnter = new HashMap<>();
  }

  public void enter(int roomId)
    throws InterruptedException {
    long threadId = Thread.currentThread().getId();
    this.mutex.lock();
    try {
      roomThreadWantsToEnter.put(threadId, roomId);
      while (hayOtroCuartoOcupado(roomId) ||
             (!waitingList.isEmpty() &&
              (waitingList.get(0) != threadId)
              || losQueEstanAntesDeMiEnWaitingListQuierenEntrarAlMismoCuartoQueYo())
             // si alguien llego antes que mi
             // y "quiere" entrar a otro cuarto (!= roomId)
             ) {
        if (!waitingList.contains(threadId)) {
          waitingList.add(threadId);
        }
        inWaitingList.await();
      }
      waitingList.remove(threadId);
      threadRoom.set(roomId);
      threadsInRoom[roomId]++;
      activeRoomId = roomId;
      // en caso de que haya mas threads que quieren
      // entrar al mismo cuarto que yo
      // inWaitingList.signalAll();
    } finally {
      this.mutex.unlock();
    }
  }

  private boolean losQueEstanAntesDeMiEnWaitingListQuierenEntrarAlMismoCuartoQueYo() {
    long myThreadId = Thread.currentThread().getId();
    for (long threadId : waitingList) {
      if (threadId == myThreadId) {
        return true;
      }
      int roomOtherThreadWantsToEnter =
        roomThreadWantsToEnter.get(threadId);
      int roomIWantToEnter =
         roomThreadWantsToEnter.get(myThreadId);
      if (roomIWantToEnter != roomOtherThreadWantsToEnter) {
        return false;
      }
    }
    return false;
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
        inWaitingList.signalAll();
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
