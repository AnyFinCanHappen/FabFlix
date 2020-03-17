package edu.uci.ics.sidneyjt.service.billing.configs;

import java.util.Map;

public class ConfigsModel {
    private Map<String,String> serviceConfig;
    private Map<String,String> loggerConfig;
    private Map<String,String> databaseConfig;
    private Map<String,String> idmConfig;
    private Map<String,String> idmEndpoints;
    private Map<String, String> moviesConfig;
    private Map<String, String> moviesEndpoints;

    public ConfigsModel() { }

    public Map<String, String> getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(Map<String, String> serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public Map<String, String> getLoggerConfig() {
        return loggerConfig;
    }

    public void setLoggerConfig(Map<String, String> loggerConfig) {
        this.loggerConfig = loggerConfig;
    }

    public Map<String, String> getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(Map<String, String> databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public Map<String, String> getIdmConfig() {
        return idmConfig;
    }

    public void setIdmConfig(Map<String, String> idmConfig) {
        this.idmConfig = idmConfig;
    }

    public Map<String, String> getIdmEndpoints() {
        return idmEndpoints;
    }

    public void setIdmEndpoints(Map<String, String> idmEndpoints) {
        this.idmEndpoints = idmEndpoints;
    }

    public Map<String, String> getMoviesConfig() {
        return moviesConfig;
    }

    public void setMoviesConfig(Map<String, String> moviesConfig) {
        this.moviesConfig = moviesConfig;
    }

    public Map<String, String> getMoviesEndpoints() {
        return moviesEndpoints;
    }

    public void setMoviesEndpoints(Map<String, String> moviesEndpoints) {
        this.moviesEndpoints = moviesEndpoints;
    }
}