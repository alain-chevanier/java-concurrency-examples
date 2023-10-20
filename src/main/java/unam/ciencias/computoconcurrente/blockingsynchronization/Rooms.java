package unam.ciencias.computoconcurrente.blockingsynchronization;

public interface Rooms {
  void enter(int roomId) throws InterruptedException;

  boolean exit();

  void setExitHandlers(int roomId, Runnable exitHandler);

  int countThreadsInRoom(int roomId);

  int[] getThreadsInRooms();
}
