package edu.uci.ics.sidneyjt.service.gateway.threadpool;

import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;

import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPool
{
    private int numWorkers;

    private ArrayList<Worker> workers;
    private BlockingQueue<ClientRequest> queue;
    private int workersRunning;

    //add queue later to know which workers are ready.
    /*
     * BlockingQueue is a interface that allows us
     * to choose the type of implementation of the queue.
     * In this case we are using a LinkedBlockingQueue.
     *
     * BlockingQueue as the name implies will block
     * any thread requesting from it if the queue is empty
     * but only if you use the correct function
     */
    private ThreadPool(int numWorkers)
    {
        this.numWorkers = numWorkers;
        this.workersRunning = 0;

        workers = new ArrayList<>();
        queue = new LinkedBlockingQueue<>();

        for(int i = 0; i < numWorkers; i ++)
        {
            workers.add(Worker.CreateWorker(i, this));
        }
        // TODO more work is needed to create the threads
    }

    public static ThreadPool createThreadPool(int numWorkers)
    {
        return new ThreadPool(numWorkers);
    }

    /*
     * Note that this function only has package scoped
     * as it should only be called with the package by
     * a worker
     * 
     * Make sure to use the correct functions that will
     * block a thread if the queue is unavailable or empty
     */
    synchronized ClientRequest takeRequest() throws InterruptedException
    {
        if(queue.isEmpty())
            throw new InterruptedException("Queue empty.");
        return queue.poll();
    }

    public synchronized void putRequest(ClientRequest clientRequest) throws InterruptedException
    {
        this.queue.put(clientRequest);
        try {
            if(this.workersRunning < numWorkers) {
                ServiceLogger.LOGGER.info("Get worker:" + this.workersRunning);
                workers.get(this.workersRunning).start();
                this.workersRunning++;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
