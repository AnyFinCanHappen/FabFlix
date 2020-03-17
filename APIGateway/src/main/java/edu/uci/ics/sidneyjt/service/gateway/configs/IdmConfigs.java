package edu.uci.ics.sidneyjt.service.gateway.configs;

import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;

public class IdmConfigs {

    // These configs are complete

    public static final int MIN_SERVICE_PORT = 1024;
    public static final int MAX_SERVICE_PORT = 65535;

    // Default IDM configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 6243;
    private final String DEFAULT_PATH = "/api/idm";

    // IDM configs
    private String scheme;
    private String hostName;
    private int port;
    private String path;

    // IDM endpoints
    private String registerPath;
    private String loginPath;
    private String sessionPath;
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

            registerPath = cm.getIdmEndpoints().get("register");
            if (registerPath == null) {
                System.err.println("IDM Register Path not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Register Path: " + registerPath);
            }

            loginPath = cm.getIdmEndpoints().get("login");
            if (loginPath == null) {
                System.err.println("IDM Login Path not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Login Path: " + loginPath);
            }

            sessionPath = cm.getIdmEndpoints().get("session");
            if (sessionPath == null) {
                System.err.println("IDM Session Path not found in configuration file. Using default.");
            } else {
                System.err.println("IDM Session Path: " + sessionPath);
            }

            privilegePath = cm.getIdmEndpoints().get("privilege");
            if (privilegePath == null) {
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
        ServiceLogger.LOGGER.config("IDM Endpoints: \n" +
                "\t" + registerPath + "\n" +
                "\t" + loginPath + "\n" +
                "\t" + sessionPath + "\n" +
                "\t" + privilegePath);
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

    public String getRegisterPath() {
        return registerPath;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public String getSessionPath() {
        return sessionPath;
    }

    public String getPrivilegePath() {
        return privilegePath;
    }
}