package com.mscg.jmp3.util.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private static final ThreadPoolManager instance = new ThreadPoolManager();

    protected ThreadPoolExecutor threadPool;

    /**
     * @return the instance
     */
    public static ThreadPoolManager getInstance() {
        return instance;
    }

    private ThreadPoolManager() {
        threadPool = new ThreadPoolExecutor(1, 1, 300, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void execute(Runnable command) {
        threadPool.execute(command);
    }

}
