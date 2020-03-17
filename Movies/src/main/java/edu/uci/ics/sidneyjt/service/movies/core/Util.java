package edu.uci.ics.sidneyjt.service.movies.core;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.sidneyjt.service.movies.MoviesService;
import edu.uci.ics.sidneyjt.service.movies.configs.IdmConfigs;
import edu.uci.ics.sidneyjt.service.movies.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.movies.models.base.RequestModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.movies.models.base.Result;
import org.glassfish.jersey.jackson.JacksonFeature;


import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class Util
{
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static <T, S extends ResponseModel> T modelMapper(
            String jsonString, Class<T> className, S responseModel)
    {
        ServiceLogger.LOGGER.info("Mapping object from String");
        try {
            return MAPPER.readValue(jsonString, className);
        } catch (IOException e) {
            setException(e, responseModel);
        }
        ServiceLogger.LOGGER.info("Mapping Object Failed: " + responseModel.getResult());
        return null;
    }

    private static <S extends ResponseModel> void setException(IOException e, S responseModel)
    {
        if (e instanceof JsonMappingException) {
            responseModel.setResult(Result.JSON_MAPPING_EXCEPTION);

        } else if (e instanceof JsonParseException) {
            responseModel.setResult(Result.JSON_PARSE_EXCEPTION);

        } else {
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);

        }
    }

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

    public static <T, S extends ResponseModel> T checkUserPLevel(String servicePath, String endpointPath, Class<T> className, RequestModel requestModel)
    {


        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(servicePath).path(endpointPath);

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");
        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        //for this example we map to JSON, but usually map to another model.
        String jsonText = response.readEntity(String.class);
        ServiceLogger.LOGGER.info(jsonText);
        return modelMapper(jsonText, className);
    }

    public static Boolean setResultFromUserPLevel(ResponseModel responseModel)
    {
        if(responseModel != null)
        {
            if(responseModel.getResultCode() == Result.SUFFICIENT_P_LEVEL.getResultCode()) {
                ServiceLogger.LOGGER.info("User has sufficient p_level.");
                responseModel.setResult(Result.SUFFICIENT_P_LEVEL);
                return false;
            }
            else if(responseModel.getResultCode() == Result.INSUFFICIENT_P_LEVEL.getResultCode()) {
                ServiceLogger.LOGGER.info("User has insufficient p_level.");
                responseModel.setResult(Result.INSUFFICIENT_P_LEVEL);
                return  true;
            }
            else {
                responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        else {
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    public static String getServicePath()
    {
         return MoviesService.getIdmConfigs().getScheme() + MoviesService.getIdmConfigs().getHostName() + ":" +
                MoviesService.getIdmConfigs().getPort() + MoviesService.getIdmConfigs().getPath();
    }

    public static String getEndpointPath()
    {
        return MoviesService.getIdmConfigs().getPrivilegePath();
    }



}
