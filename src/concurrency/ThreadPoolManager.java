package concurrency;

import java.util.concurrent.*;

public class ThreadPoolManager {
    private static ThreadPoolManager instance;
    private final ThreadPoolExecutor executor;
    
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final long KEEP_ALIVE_TIME = 60L;
    
    private ThreadPoolManager() {
        executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    
    public static synchronized ThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new ThreadPoolManager();
        }
        return instance;
    }
    
    public <T> Future<T> submitTask(Callable<T> task) {
        return executor.submit(task);
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public int getActiveThreadCount() {
        return executor.getActiveCount();
    }
    
    public int getQueueSize() {
        return executor.getQueue().size();
    }
    
    public int getPoolSize() {
        return executor.getPoolSize();
    }
    
    public long getCompletedTaskCount() {
        return executor.getCompletedTaskCount();
    }
} 