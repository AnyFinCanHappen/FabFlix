package edu.uci.ics.sidneyjt.service.billing.configs;

import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;

public class IdmConfigs {

    // These configs are complete

    public static final int MIN_SERVICE_PORT = 1024;
    public static final int MAX_SERVICE_PORT = 65535;

    // Default IDM configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 6243;
    private final String DEFAULT_PATH = "/api/idm";
    private final String DEFAULT_PRIVILEGE = "/privilege";

    // IDM configs
    private String scheme;
    private String hostName;
    private int port;
    private String path;
    private String privilegePath;

    public IdmConfigs() {
    }

    public IdmConfigs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            ServiceLogger.LOGGER.severe("ConfigsModel not found.");
            throw new NullPointerException("ConfigsModel not found.");
        } else {
            // Set IDM configs
            scheme = cm.getIdmConfig().get("scheme");
            if (scheme == null) {
                scheme = DEFAULT_SCHEME;
                System.err.println("IDM Scheme not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Scheme: " + scheme);
            }

            hostName = cm.getIdmConfig().get("hostName");
            if (hostName == null) {
                hostName = DEFAULT_HOSTNAME;
                System.err.println("IDM Hostname not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Hostname: " + hostName);
            }

            port = Integer.parseInt(cm.getIdmConfig().get("port"));
            if (port == 0) {
                port = DEFAULT_PORT;
                System.err.println("IDM Port not found in configuration file. Using default.");
            } else if (port < MIN_SERVICE_PORT || port > MAX_SERVICE_PORT) {
                port = DEFAULT_PORT;
                System.err.println("IDM Port is not within valid range. Using default.");
            } else {
                System.err.println("IDM Port: " + port);
            }

            path = cm.getIdmConfig().get("path");
            if (path == null) {
                path = DEFAULT_PATH;
                System.err.println("IDM Path not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Path: " + path);
            }

            privilegePath = cm.getIdmEndpoints().get("privilege");
            if (privilegePath == null) {
                path = DEFAULT_PRIVILEGE;
                System.err.println("IDM Privilege path not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Privilege Path: " + privilegePath);
            }
        }
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("IDM Scheme: " + scheme);
        ServiceLogger.LOGGER.config("IDM Hostname: " + hostName);
        ServiceLogger.LOGGER.config("IDM Port: " + port);
        ServiceLogger.LOGGER.config("IDM Path: " + path);
        ServiceLogger.LOGGER.config("IDM Privilege Path: " + privilegePath);
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

    public String getPrivilegePath() {
        return privilegePath;
    }
}