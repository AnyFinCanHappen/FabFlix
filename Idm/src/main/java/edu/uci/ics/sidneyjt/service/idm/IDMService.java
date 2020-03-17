package edu.uci.ics.sidneyjt.service.idm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import edu.uci.ics.sidneyjt.service.idm.configs.ConfigsModel;
import edu.uci.ics.sidneyjt.service.idm.configs.ServiceConfigs;
import edu.uci.ics.sidneyjt.service.idm.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.idm.security.Session;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IDMService {

    public static final String RESOURCE_PATH = "edu.uci.ics.sidneyjt.service.idm.resources";

    public static IDMService service;
    private static ServiceConfigs serviceConfigs;

    private static Connection con = null;

    public static void main(String[] args) {
        service = new IDMService();
        service.initService(args);
    }

    private void initService(String[] args) {
        // Validate arguments
        validateArguments(args);

        // Exec the arguments
        execArguments(args);

        // Initialize logging
        initLogging();
        ServiceLogger.LOGGER.config("Starting service...");
        serviceConfigs.currentConfigs();

        // Create a connection to the database
        connectToDatabase();

        // Initialize HTTP sever
        initHTTPServer();

        ServiceLogger.LOGGER.config("Service initialized.");
    }

    private void validateArguments(String[] args) {
        boolean isConfigOptionSet = false;
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--default":
                case "-d":
                    if (i + 1 < args.length) {
                        exitAppFailure("Invalid arg after " + args[i] + " option: " + args[i + 1]);
                    }
                case "--config":
                case "-c":
                    if (!isConfigOptionSet) {
                        isConfigOptionSet = true;
                        ++i;
                    } else {
                        exitAppFailure("Conflicting configuration file arguments.");
                    }
                    break;

                default:
                    exitAppFailure("Unrecognized argument: " + args[i]);
            }
        }
    }

    private void execArguments(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                switch (args[i]) {
                    case "--config":
                    case "-c":
                        // Config file specified. Load it.
                        getConfigFile(args[i + 1]);
                        ++i;
                        break;
                    case "--default":
                    case "-d":
                        System.err.println("Default config options selected.");
                        serviceConfigs = new ServiceConfigs();
                        break;
                    default:
                        exitAppFailure("Unrecognized argument: " + args[i]);
                }
            }
        } else {
            System.err.println("No config file specified. Using default values.");
            serviceConfigs = new ServiceConfigs();
        }
    }

    private void getConfigFile(String configFile) {
        try {
            System.err.println("Config file name: " + configFile);
            serviceConfigs = new ServiceConfigs(loadConfigs(configFile));
            System.err.println("Configuration file successfully loaded.");
        } catch (NullPointerException e) {
            System.err.println("Config file not found. Using default values.");
            serviceConfigs = new ServiceConfigs();
        }
    }

    private ConfigsModel loadConfigs(String file) {
        System.err.println("Loading configuration file...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfigsModel configs = null;

        try {
            configs = mapper.readValue(new File(file), ConfigsModel.class);
        } catch (IOException e) {
            exitAppFailure("Unable to load configuration file.");
        }
        return configs;
    }

    private void initLogging() {
        try {
            ServiceLogger.initLogger(serviceConfigs.getOutputDir(), serviceConfigs.getOutputFile());
        } catch (IOException e) {
            exitAppFailure("Unable to initialize logging.");
        }
    }

    private void connectToDatabase() {
        ServiceLogger.LOGGER.config("Connecting to database...");

        if (!serviceConfigs.isDbConfigValid()) {
            ServiceLogger.LOGGER.config("Database configurations not valid. Cannot connect to database.");
            return;
        }

        try {
            Class.forName(serviceConfigs.getDbDriver());
            ServiceLogger.LOGGER.config("Database URL: " + serviceConfigs.getDbUrl());
            con = DriverManager.getConnection(serviceConfigs.getDbUrl(), serviceConfigs.getDbUsername(), serviceConfigs.getDbPassword());
            ServiceLogger.LOGGER.config("Connected to database: " + serviceConfigs.getDbUrl());
        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));
        }
    }

    private void initHTTPServer() {
        ServiceLogger.LOGGER.config("Initializing HTTP server...");
        String scheme = serviceConfigs.getScheme();
        String hostName = serviceConfigs.getHostName();
        int port = serviceConfigs.getPort();
        String path = serviceConfigs.getPath();
        long timeout = serviceConfigs.getTimeout();
        long expiration = serviceConfigs.getExpiration();

        try {
            ServiceLogger.LOGGER.config("Building URI from configs...");
            URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
            ServiceLogger.LOGGER.config("Final URI: " + uri.toString());

            ResourceConfig rc = new ResourceConfig().packages(RESOURCE_PATH);
            ServiceLogger.LOGGER.config("Set Jersey resources.");

            rc.register(JacksonFeature.class);
            ServiceLogger.LOGGER.config("Set Jackson as serializer.");

            Session.SESSION_TIMEOUT = timeout;
            Session.TOKEN_EXPR = expiration;
            ServiceLogger.LOGGER.config("Set session timeout and token expiration.");

            ServiceLogger.LOGGER.config("Starting HTTP server...");
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
            server.start();
            ServiceLogger.LOGGER.config("HTTP server started.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void exitAppFailure(String message) {
        System.err.println("ERROR: " + message);
        System.err.println("Usage options: " );
        System.err.println("\tSpecify configuration file:");
        System.err.println("\t\t--config [file]");
        System.err.println("\t\t-c [file]");
        System.err.println("\tUse default configuration:");
        System.err.println("\t\t--default");
        System.err.println("\t\t-d");
        System.exit(-1);
    }

    public static Connection getCon() {
        return con;
    }

}
