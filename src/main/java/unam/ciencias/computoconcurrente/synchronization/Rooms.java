package unam.ciencias.computoconcurrente.synchronization;

public interface Rooms {
  void enter(int roomId) throws InterruptedException;

  boolean exit();

  void setExitHandlers(int roomId, Runnable exitHandler);

  int countThreadsInRoom(int roomId);

  int[] getThreadsInRooms();
}
