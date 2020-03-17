package edu.uci.ics.sidneyjt.service.gateway.configs;

import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;

public class BillingConfigs {

    // These configs are complete

    public static final int MIN_SERVICE_PORT = 1024;
    public static final int MAX_SERVICE_PORT = 65535;

    // Default Billing configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 6243;
    private final String DEFAULT_PATH = "/api/billing";

    // Billing configs
    private String scheme;
    private String hostName;
    private int port;
    private String path;

    // Billing endpoints
    private String cartInsertPath;
    private String cartUpdatePath;
    private String cartDeletePath;
    private String cartRetrievePath;
    private String cartClearPath;
    private String orderPlacePath;
    private String orderRetrievePath;
    private String orderCompletePath;

    public BillingConfigs() {
    }

    public BillingConfigs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            ServiceLogger.LOGGER.severe("ConfigsModel not found.");
            throw new NullPointerException("ConfigsModel not found.");
        } else {
            // Set Billing configs
            scheme = cm.getBillingConfig().get("scheme");
            if (scheme == null) {
                scheme = DEFAULT_SCHEME;
                System.err.println("Billing Scheme not found in configuration file. Using default.");
            } else {
                System.err.println("Billing Scheme: " + scheme);
            }

            hostName = cm.getBillingConfig().get("hostName");
            if (hostName == null) {
                hostName = DEFAULT_HOSTNAME;
                System.err.println("Billing Hostname not found in configuration file. Using default.");
            } else {
                System.err.println("Billing Hostname: " + hostName);
            }

            port = Integer.parseInt(cm.getBillingConfig().get("port"));
            if (port == 0) {
                port = DEFAULT_PORT;
                System.err.println("Billing Port not found in configuration file. Using default.");
            } else if (port < MIN_SERVICE_PORT || port > MAX_SERVICE_PORT) {
                port = DEFAULT_PORT;
                System.err.println("Billing Port is not within valid range. Using default.");
            } else {
                System.err.println("Billing Port: " + port);
            }

            path = cm.getBillingConfig().get("path");
            if (path == null) {
                path = DEFAULT_PATH;
                System.err.println("Billing Path not found in configuration file. Using default.");
            } else {
                System.err.println("Billing Path: " + path);
            }

            cartInsertPath = cm.getBillingEndpoints().get("cartInsert");
            if (cartInsertPath == null) {
                System.err.println("Billing Cart Insert Path not found in configuration file.");
            } else {
                System.err.println("Billing Cart Insert Path: " + cartInsertPath);
            }

            cartUpdatePath = cm.getBillingEndpoints().get("cartUpdate");
            if (cartUpdatePath == null) {
                System.err.println("Billing Cart Update Path not found in configuration file.");
            } else {
                System.err.println("Billing Cart Update Path: " + cartUpdatePath);
            }

            cartDeletePath = cm.getBillingEndpoints().get("cartDelete");
            if (cartDeletePath == null) {
                System.err.println("Billing Cart Delete Path not found in configuration file.");
            } else {
                System.err.println("Billing Cart Delete Path: " + cartDeletePath);
            }

            cartRetrievePath = cm.getBillingEndpoints().get("cartRetrieve");
            if (cartRetrievePath == null) {
                System.err.println("Billing Cart Retrieve Path not found in configuration file.");
            } else {
                System.err.println("Billing Cart Retrieve Path: " + cartRetrievePath);
            }

            cartClearPath = cm.getBillingEndpoints().get("cartClear");
            if (cartClearPath == null) {
                System.err.println("Billing Cart Clear Path not found in configuration file.");
            } else {
                System.err.println("Billing Cart Clear Path: " + cartClearPath);
            }

            orderPlacePath = cm.getBillingEndpoints().get("orderPlace");
            if (orderPlacePath == null) {
                System.err.println("Billing Order Place Path not found in configuration file.");
            } else {
                System.err.println("Billing Order Place Path: " + orderPlacePath);
            }

            orderRetrievePath = cm.getBillingEndpoints().get("orderRetrieve");
            if (orderRetrievePath == null) {
                System.err.println("Billing Order Retrieve Path not found in configuration file.");
            } else {
                System.err.println("Billing Order Retrieve Path: " + orderRetrievePath);
            }

            orderCompletePath = cm.getBillingEndpoints().get("orderComplete");
            if (orderCompletePath == null) {
                System.err.println("Billing Order Complete Path not found in configuration file.");
            } else {
                System.err.println("Billing Order Complete Path: " + orderCompletePath);
            }
        }
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("Billing Scheme: " + scheme);
        ServiceLogger.LOGGER.config("Billing Hostname: " + hostName);
        ServiceLogger.LOGGER.config("Billing Port: " + port);
        ServiceLogger.LOGGER.config("Billing Path: " + path);
        ServiceLogger.LOGGER.config("Billing Endpoints: \n" +
                                    "\t" + cartInsertPath + "\n" +
                                    "\t" + cartUpdatePath + "\n" +
                                    "\t" + cartDeletePath + "\n" +
                                    "\t" + cartRetrievePath + "\n" +
                                    "\t" + cartClearPath + "\n" +
                                    "\t" + orderPlacePath + "\n" +
                                    "\t" + orderRetrievePath +"\n" +
                                    "\t" + orderCompletePath);
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getCartInsertPath() {
        return cartInsertPath;
    }

    public String getCartUpdatePath() {
        return cartUpdatePath;
    }

    public String getCartDeletePath() {
        return cartDeletePath;
    }

    public String getCartRetrievePath() {
        return cartRetrievePath;
    }

    public String getCartClearPath() {
        return cartClearPath;
    }

    public String getOrderPlacePath() {
        return orderPlacePath;
    }

    public String getOrderRetrievePath() {
        return orderRetrievePath;
    }

    public String getOrderCompletePath() {
        return orderCompletePath;
    }
}