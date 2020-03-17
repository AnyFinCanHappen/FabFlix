package edu.uci.ics.sidneyjt.service.gateway.configs;

import java.util.Map;

public class ConfigsModel {
    private Map<String,String> serviceConfig;
    private Map<String,String> loggerConfig;
    private Map<String,String> databaseConfig;
    private Map<String,String> threadConfig;
    private Map<String,String> idmConfig;
    private Map<String,String> idmEndpoints;
    private Map<String, String> moviesConfig;
    private Map<String, String> moviesEndpoints;
    private Map<String, String> billingConfig;
    private Map<String, String> billingEndpoints;

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

    public Map<String, String> getThreadConfig() {
        return threadConfig;
    }

    public void setThreadConfig(Map<String, String> threadConfig) {
        this.threadConfig = threadConfig;
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

    public Map<String, String> getBillingConfig() {
        return billingConfig;
    }

    public void setBillingConfig(Map<String, String> billingConfig) {
        this.billingConfig = billingConfig;
    }

    public Map<String, String> getBillingEndpoints() {
        return billingEndpoints;
    }

    public void setBillingEndpoints(Map<String, String> billingEndpoints) {
        this.billingEndpoints = billingEndpoints;
    }
}