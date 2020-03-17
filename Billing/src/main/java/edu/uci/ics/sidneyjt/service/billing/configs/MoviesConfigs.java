package edu.uci.ics.sidneyjt.service.billing.configs;

import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;

public class MoviesConfigs {

    // These configs are complete

    public static final int MIN_SERVICE_PORT = 1024;
    public static final int MAX_SERVICE_PORT = 65535;

    // Default Movies configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 6243;
    private final String DEFAULT_PATH = "/api/movies";
    private final String DEFAULT_PRIVILEGE = "/thumbnail";

    // Movies configs
    private String scheme;
    private String hostName;
    private int port;
    private String path;
    private String thumbnailPath;

    public MoviesConfigs() {
    }

    public MoviesConfigs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            ServiceLogger.LOGGER.severe("ConfigsModel not found.");
            throw new NullPointerException("ConfigsModel not found.");
        } else {
            // Set IDM configs
            scheme = cm.getMoviesConfig().get("scheme");
            if (scheme == null) {
                scheme = DEFAULT_SCHEME;
                System.err.println("Movies Scheme not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Scheme: " + scheme);
            }

            hostName = cm.getMoviesConfig().get("hostName");
            if (hostName == null) {
                hostName = DEFAULT_HOSTNAME;
                System.err.println("Movies Hostname not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Hostname: " + hostName);
            }

            port = Integer.parseInt(cm.getMoviesConfig().get("port"));
            if (port == 0) {
                port = DEFAULT_PORT;
                System.err.println("Movies Port not found in configuration file. Using default.");
            } else if (port < MIN_SERVICE_PORT || port > MAX_SERVICE_PORT) {
                port = DEFAULT_PORT;
                System.err.println("Movies Port is not within valid range. Using default.");
            } else {
                System.err.println("Movies Port: " + port);
            }

            path = cm.getMoviesConfig().get("path");
            if (path == null) {
                path = DEFAULT_PATH;
                System.err.println("Movies Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Path: " + path);
            }

            thumbnailPath = cm.getMoviesEndpoints().get("thumbnail");
            if (thumbnailPath == null) {
                path = DEFAULT_PRIVILEGE;
                System.err.println("Movies Thumbnail path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Thumbnail Path: " + thumbnailPath);
            }
        }
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("Movies Scheme: " + scheme);
        ServiceLogger.LOGGER.config("Movies Hostname: " + hostName);
        ServiceLogger.LOGGER.config("Movies Port: " + port);
        ServiceLogger.LOGGER.config("Movies Path: " + path);
        ServiceLogger.LOGGER.config("Movies Thumbnail Path: " + thumbnailPath);
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }
}