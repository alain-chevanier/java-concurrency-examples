package unam.ciencias.computoconcurrente;

public class SimpleThreads {

    // Display a message, preceded by
    // the name of the current thread


    public static void example(String args[])
            throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 2;//60 * 60;

        // If command line argument
        // present, gives patience
        // in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();

        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            if (((System.currentTimeMillis() - startTime) > patience)
                    && t.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
            }
        }
        threadMessage("Finally!");
    }

    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    private static class MessageLoop
            implements Runnable {
        public void run() {
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };
            // espera utilizando sleep
//            try {
//                for (int i = 0; i < importantInfo.length; i++) {
//                    // Pause for 4 seconds
//                    Thread.sleep(4000);
//                    // Print a message
//                    threadMessage(importantInfo[i]);
//                }
//            } catch (InterruptedException e) {
//                threadMessage("I wasn't done!");
//            }

            // espera utilizando un ciclo de computo muy largo
            for (int i = 0; i < importantInfo.length; i++) {
                // Esta es una
                for (int j = 0; j < 1000000000; j++) {
                    // antes de proceder verificamos si alguien nos ha interrumpido
                    if (Thread.interrupted()) {
                        // Idealmente esto es lo que tenemos que hacer:
                        // throw new InterruptedException();
                        threadMessage("I wasn't done!");
                        return;
                    }
                    if (j == (1000000000 - 1)) {
                        threadMessage("Finish sleeping");
                    }
                }

                // Print a message
                threadMessage(importantInfo[i]);
            }
        }
    }
}