package thread.test;

import java.util.concurrent.*;

public class ThreadPoolExample {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10, // corePoolSize
                50, // maximumPoolSize
                60, // keepAliveTime
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100) // bounded queue
        );

        // Submit tasks
        for (int i = 0; i < 150; i++) {
            int taskId = i;
            executor.submit(() -> {
                // Simulate task
                System.out.println("Task " + taskId + " is running.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
    }
}

