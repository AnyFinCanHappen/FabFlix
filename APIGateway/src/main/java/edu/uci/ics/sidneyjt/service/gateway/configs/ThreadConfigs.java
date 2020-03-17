package edu.uci.ics.sidneyjt.service.gateway.configs;

import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;

public class ThreadConfigs {

    // These configs are complete

    private final int DEFAULT_NUM_THREADS = 10;
    private final long DEFAULT_REQUEST_DELAY = 5;

    // IDM configs
    private int numThreads;
    private long requestDelay;

    public ThreadConfigs() {
    }

    public ThreadConfigs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            ServiceLogger.LOGGER.severe("ConfigsModel not found.");
            throw new NullPointerException("ConfigsModel not found.");
        } else {
            numThreads = Integer.parseInt(cm.getThreadConfig().get("numThreads"));
            if (numThreads == 0) {
                numThreads = DEFAULT_NUM_THREADS;
                System.err.println("Number of Threads not found in configuration file. Using default.");
            } else {
                System.err.println("Number of Threads: " + numThreads);
            }

            requestDelay = Long.parseLong(cm.getThreadConfig().get("requestDelay"));
            if (requestDelay == 0) {
                requestDelay = DEFAULT_REQUEST_DELAY;
                System.err.println("Request Delay not found in configuration file. Using default.");
            } else {
                System.err.println("Request Delay: " + requestDelay);
            }

        }
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("Number of Threads: " + numThreads);
        ServiceLogger.LOGGER.config("Request Delay: " + requestDelay);
    }

    public int getNumThreads() {
        return numThreads;
    }

    public long getRequestDelay() {
        return requestDelay;
    }
}