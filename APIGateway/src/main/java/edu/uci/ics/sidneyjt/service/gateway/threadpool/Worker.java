package edu.uci.ics.sidneyjt.service.gateway.threadpool;

import edu.uci.ics.sidneyjt.service.gateway.GatewayService;
import edu.uci.ics.sidneyjt.service.gateway.connectionpool.ConnectionPoolManager;
import edu.uci.ics.sidneyjt.service.gateway.core.Util;
import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.gateway.resources.GatewayPage;

import java.sql.Connection;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;
    Connection con;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool)
    {
        return new Worker(id, threadPool);
    }

    @Override
    public void run()
    {
        ServiceLogger.LOGGER.info("Worker " + this.id + " running.");
        try {
            while(true)
            {
                try
                {
                    ClientRequest clientRequest = this.threadPool.takeRequest();
                    ServiceLogger.LOGGER.info("Client request taken with token_id: " + clientRequest.getTransaction_id());
                    this.con = GatewayService.getConnectionPoolManager().requestCon();
                    Util.sendHttpRequest(clientRequest, this.con);
                    GatewayService.getConnectionPoolManager().releaseCon(this.con);
                    ServiceLogger.LOGGER.info("Worker " + this.id + " is done.");
                } catch (InterruptedException e)
                {
                    sleep(3000);
                    //System.err.println(e.getMessage() + " Worker " + this.id + " waiting for new client requests.");
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.warning("Threading error: " + e.getMessage());
        }
    }
}
