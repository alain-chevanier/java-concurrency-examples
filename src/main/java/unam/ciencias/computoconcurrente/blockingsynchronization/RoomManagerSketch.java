package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RoomManagerSketch implement Rooms {
  final int rooms;
  final Lock mutex;
  final Condition condition;

  static final int NO_ROOM = -1;
  int activeRoom = NO_ROOM;
  int threadsInActiveRoom = 0;

  public RoomManagerSketch(int rooms) {
    this.rooms = rooms;
    this.mutex = new ReentrantLock();
    this.condition = this.mutex.newCondition();
  }

  @Override
  public void enter(int roomId) throws InterruptedException {
    this.mutex.lock();
    try {
      while (activeRoom != NO_ROOM && roomId != activeRoom) {
        condition.await();
      }
      activeRoom = roomId;
      threadsInActiveRoom++;
    } catch (Throwable e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    } finally {
      mutex.unlock();
    }

  }

  @Override
  boolean exit() {
    this.mutex.lock ();
    try {
      threadsInActiveRoom--;
      if (threadsInActiveRoom == 0) {
        this.condition.signalAll();
        activeRoom = NO_ROOM;
        return true;
      }
      return false;
    } finally {
      this.mutex.unlock();
    }
    return false;
  }

  @Override
  void setExitHandlers(int roomId, Runnable exitHandler) {

  }

  @Override
  int countThreadsInRoom(int roomId) {
    return 0;
  }

  @Override
  int[] getThreadsInRooms() {
    return null;
  }
}
