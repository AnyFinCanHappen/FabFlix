package edu.uci.ics.sidneyjt.service.gateway.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse
{
    @JsonProperty(value = "jsonBytes", required = true)
    private byte[] jsonBytes;

    @JsonCreator
    public ApiResponse(byte[] jsonBytes) {
        this.jsonBytes = jsonBytes;
    }

    public ApiResponse() {
    }

    @JsonProperty(value = "jsonBytes")
    public byte[] getJsonBytes() {
        return jsonBytes;
    }
}
