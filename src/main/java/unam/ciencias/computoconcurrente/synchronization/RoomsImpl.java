package unam.ciencias.computoconcurrente.synchronization;

import unam.ciencias.computoconcurrente.soexamples.ThreadID;

import java.util.HashMap;
import java.util.Map;

public class RoomsImpl implements Rooms {
  private final int availableRooms;
  private final int[] threadsInRoom;
  private final Runnable[] exitHandlers;

  private final Map<Integer,Integer> threadIdIsInRoom;

  public RoomsImpl(int rooms) {
    this.availableRooms = rooms;
    this.threadsInRoom = new int[rooms];
    this.threadIdIsInRoom = new HashMap<>();
    this.exitHandlers = new Runnable[rooms];
  }

  public void enter(int roomId) {
    this.threadsInRoom[roomId]++;
    this.threadIdIsInRoom.put(ThreadID.get(), roomId);
    // TODO: eventually enter room whose id is roomId
  }

  public boolean exit() {
    // TODO: current thread enters
    this.threadsInRoom[this.threadIdIsInRoom.get(ThreadID.get())]--;
    return false;
  }

  public void setExitHandlers(int roomId, Runnable exitHandler) {
    this.exitHandlers[roomId] = exitHandler;
  }

  public int countThreadsInRoom(int roomId) {
    return this.threadsInRoom[roomId];
  }

  public int[] getThreadsInRooms() {
    return this.threadsInRoom.clone();
  }
}
