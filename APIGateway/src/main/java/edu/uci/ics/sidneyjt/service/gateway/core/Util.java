package edu.uci.ics.sidneyjt.service.gateway.core;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.sidneyjt.service.gateway.GatewayService;
import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.gateway.query.Query;
import edu.uci.ics.sidneyjt.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.sidneyjt.service.gateway.threadpool.HTTPMethod;
import jdk.nashorn.internal.parser.JSONParser;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;


import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Arrays;

public class Util
{

    public static <T> T modelMapper(String jsonString, Class<T> className)
    {
        ObjectMapper mapper = new ObjectMapper();
        ServiceLogger.LOGGER.info("Mapping object");
        try {
            return mapper.readValue(jsonString, className);

        } catch (IOException e) {
            ServiceLogger.LOGGER.info("Mapping Object Failed: " + e.getMessage());
            return null;
        }
    }

    synchronized public static void sendHttpRequest(ClientRequest request, Connection con) throws Exception
    {
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        String servicePoint = setServicePoint(request.getURI());
        ServiceLogger.LOGGER.info("Building WebTarget...");
        ServiceLogger.LOGGER.info("Service Point: "+ servicePoint + request.getEndpoint());
        ServiceLogger.LOGGER.info("Endpoint: " + request.getEndpoint());
        WebTarget webTarget = client.target(servicePoint).path(request.getEndpoint());
        if(request.getQueryParam() != null && !request.getQueryParam().isEmpty()) {
            for(String key: request.getQueryParam().keySet())
            {
                ServiceLogger.LOGGER.info("Key: " + key + ". Value: " + request.getQueryParam().getFirst(key));
                webTarget = webTarget.queryParam(key,request.getQueryParam().getFirst(key));
            }
        }
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
                .header("email",request.getEmail())
                .header("session_id", request.getSession_id())
                .header("transaction_id", request.getTransaction_id());

        ServiceLogger.LOGGER.info("Sending request...");
        if(request.getMethod().toString().equals(HTTPMethod.POST.toString()))
        {
            ServiceLogger.LOGGER.info("Json sent: \n" + new String(request.getRequestBytes(), StandardCharsets.UTF_8));
            Response response = invocationBuilder.post(Entity.entity(request.getRequestBytes(), MediaType.APPLICATION_JSON));
            ServiceLogger.LOGGER.info("Request sent.");
            ServiceLogger.LOGGER.info("Received status " + response.getStatus());
            String jsonText = response.readEntity(String.class);
            //ServiceLogger.LOGGER.info(jsonText);
            updateResponseTable(jsonText, request, con);
        }
        else if (request.getMethod().toString().equals(HTTPMethod.GET.toString()))
        {
            Response response = invocationBuilder.get();
            ServiceLogger.LOGGER.info("Request sent.");
            ServiceLogger.LOGGER.info("Received status " + response.getStatus());
            String jsonText = response.readEntity(String.class);
            //ServiceLogger.LOGGER.info(jsonText);
            updateResponseTable(jsonText, request, con);
        }
    }

    private static void updateResponseTable(String jsonText, ClientRequest client, Connection con) throws Exception
    {
        JSONObject jsonObject = new JSONObject(jsonText);
        Integer resultCode = jsonObject.getInt("resultCode");
        //String message = jsonObject.getString("message");
        Query.updateResponseTable(client, jsonText,resultCode, con);
    }

    private static String setServicePoint(String service)
    {
        if(service.equals("movies"))
        {
            return GatewayService.getMoviesConfigs().getScheme() + GatewayService.getMoviesConfigs().getHostName() + ":" +
                    GatewayService.getMoviesConfigs().getPort() + GatewayService.getMoviesConfigs().getPath() + "/";
        }
        else if(service.equals("idm"))
        {
            return GatewayService.getIdmConfigs().getScheme() + GatewayService.getIdmConfigs().getHostName() + ":" +
                    GatewayService.getIdmConfigs().getPort() + GatewayService.getIdmConfigs().getPath() + "/";
        }
        else if(service.equals("billing"))
        {
            return GatewayService.getBillingConfigs().getScheme() + GatewayService.getBillingConfigs().getHostName() + ":" +
                    GatewayService.getBillingConfigs().getPort() + GatewayService.getBillingConfigs().getPath() + "/";
        }
        return null;
    }

}
