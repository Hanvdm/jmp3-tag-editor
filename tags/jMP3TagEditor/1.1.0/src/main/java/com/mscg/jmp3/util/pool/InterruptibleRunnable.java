package com.mscg.jmp3.util.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class InterruptibleRunnable implements Runnable {

    protected boolean interrupted;
    protected boolean running;
    protected Logger LOG;

    public InterruptibleRunnable() {
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    public synchronized void interrupt() {
        interrupted = true;
    }

    public synchronized void blockingInterrupt() throws InterruptedException {
        interrupt();
        if(isRunning())
            this.wait();
    }

    public synchronized boolean isInterrupted() {
        return interrupted;
    }

    /**
     * @return the running
     */
    protected synchronized boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    protected synchronized void setRunning(boolean running) {
        this.running = running;
    }

    public abstract void executeInterruptible();

    @Override
    public void run() {
        setRunning(true);
        try {
            executeInterruptible();
        } finally {
            setRunning(false);
            try {
                this.notifyAll();
            } catch(Exception e){}
        }

    }

}
