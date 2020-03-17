package edu.uci.ics.sidneyjt.service.gateway.configs;

import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;

public class MoviesConfigs {

    // These configs are complete

    public static final int MIN_SERVICE_PORT = 1024;
    public static final int MAX_SERVICE_PORT = 65535;

    // Default Movies configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 6243;
    private final String DEFAULT_PATH = "/api/movies";

    // Movies configs
    private String scheme;
    private String hostName;
    private int port;
    private String path;

    // Movies endpoints
    private String searchPath;
    private String browsePath;
    private String getPath;
    private String thumbnailPath;
    private String peoplePath;
    private String peopleSearchPath;
    private String peopleGetPath;

    public MoviesConfigs() {
    }

    public MoviesConfigs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            ServiceLogger.LOGGER.severe("ConfigsModel not found.");
            throw new NullPointerException("ConfigsModel not found.");
        } else {
            // Set Movies configs
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

            searchPath = cm.getMoviesEndpoints().get("search");
            if (searchPath == null) {
                System.err.println("Movies Search Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Search Path: " + searchPath);
            }

            browsePath = cm.getMoviesEndpoints().get("browse");
            if (browsePath == null) {
                System.err.println("Movies Browse Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Browse Path: " + browsePath);
            }

            getPath = cm.getMoviesEndpoints().get("get");
            if (getPath == null) {
                System.err.println("Movies Get Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Get Path: " + getPath);
            }

            thumbnailPath = cm.getMoviesEndpoints().get("thumbnail");
            if (thumbnailPath == null) {
                System.err.println("Movies Thumbnail Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies Thumbnail Path: " + thumbnailPath);
            }

            peoplePath = cm.getMoviesEndpoints().get("people");
            if (peoplePath == null) {
                System.err.println("Movies People Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies People Path: " + peoplePath);
            }

            peopleSearchPath = cm.getMoviesEndpoints().get("peopleSearch");
            if (peopleSearchPath == null) {
                System.err.println("Movies People Search Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies People Search Path: " + peopleSearchPath);
            }

            peopleGetPath = cm.getMoviesEndpoints().get("peopleGet");
            if (peopleGetPath == null) {
                System.err.println("Movies People Get Path not found in configuration file. Using default.");
            } else {
                System.err.println("Movies People Get Path: " + peopleGetPath);
            }
        }
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("Movies Scheme: " + scheme);
        ServiceLogger.LOGGER.config("Movies Hostname: " + hostName);
        ServiceLogger.LOGGER.config("Movies Port: " + port);
        ServiceLogger.LOGGER.config("Movies Path: " + path);
        ServiceLogger.LOGGER.config("Movies Endpoints: \n" +
                "\t" + searchPath + "\n" +
                "\t" + browsePath + "\n" +
                "\t" + getPath + "\n" +
                "\t" + thumbnailPath + "\n" +
                "\t" + peoplePath + "\n" +
                "\t" + peopleSearchPath + "\n" +
                "\t" + peopleGetPath);
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

    public String getSearchPath() {
        return searchPath;
    }

    public String getBrowsePath() {
        return browsePath;
    }

    public String getGetPath() {
        return getPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getPeoplePath() {
        return peoplePath;
    }

    public String getPeopleSearchPath() {
        return peopleSearchPath;
    }

    public String getPeopleGetPath() {
        return peopleGetPath;
    }
}