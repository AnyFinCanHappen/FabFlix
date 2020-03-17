package edu.uci.ics.sidneyjt.service.gateway.threadpool;


import edu.uci.ics.sidneyjt.service.gateway.models.ApiResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import javax.ws.rs.HttpMethod;
import java.awt.*;

public class ClientRequest
{
    /* User Information */
    private String email;
    private String session_id;
    private String transaction_id;

    /* Target Service and Endpoint */
    private String URI;
    private String endpoint;
    private HTTPMethod method;
    private MultivaluedMap<String,String> queryParam;

    /*
     * So before when we wanted to get the request body
     * we would grab it as a String (String jsonText).
     *
     * The Gateway however does not need to see the body
     * but simply needs to pass it. So we save ourselves some
     * time and overhead by grabbing the request as a byte array
     * (byte[] jsonBytes).
     *
     * This way we can just act as a
     * messenger and just pass along the bytes to the target
     * service and it will do the rest.
     *
     * for example:
     *
     * where we used to do this:
     *
     *     @Path("hello")
     *     ...ect
     *     public Response hello(String jsonString) {
     *         ...ect
     *     }
     *
     * do:
     *
     *     @Path("hello")
     *     ...ect
     *     public Response hello(byte[] jsonBytes) {
     *         ...ect
     *     }
     *
     */
    private byte[] requestBytes;

    public ClientRequest()
    {
    }

    public ClientRequest(String email, String session_id, String transaction_id, String URI, String endpoint, HTTPMethod method, byte[] requestBytes, MultivaluedMap<String, String> queryParam) {
        this.email = email;
        this.session_id = session_id;
        this.transaction_id = transaction_id;
        this.URI = URI;
        this.endpoint = endpoint;
        this.method = method;
        this.requestBytes = requestBytes;
        this.queryParam = queryParam;
    }

    public String getEmail() {
        return email;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getURI() {
        return URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public byte[] getRequestBytes() {
        return requestBytes;
    }

    public MultivaluedMap<String, String> getQueryParam() {
        return queryParam;
    }
}
