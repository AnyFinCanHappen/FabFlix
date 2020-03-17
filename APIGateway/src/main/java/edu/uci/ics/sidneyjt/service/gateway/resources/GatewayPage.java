package edu.uci.ics.sidneyjt.service.gateway.resources;

import edu.uci.ics.sidneyjt.service.gateway.GatewayService;
import edu.uci.ics.sidneyjt.service.gateway.core.Util;
import edu.uci.ics.sidneyjt.service.gateway.models.ReportResponse;
import edu.uci.ics.sidneyjt.service.gateway.models.Result;
import edu.uci.ics.sidneyjt.service.gateway.models.SessionRequestModel;
import edu.uci.ics.sidneyjt.service.gateway.models.SessionResponseModel;
import edu.uci.ics.sidneyjt.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.sidneyjt.service.gateway.threadpool.HTTPMethod;
import edu.uci.ics.sidneyjt.service.gateway.logger.ServiceLogger;
import edu.uci.ics.sidneyjt.service.gateway.transaction.TransactionGenerator;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.client.*;

@Path("{all}/{end}")
public class GatewayPage
{
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTwoPaths(byte[] jsonBytes, @Context HttpHeaders headers, @PathParam("all") String all, @PathParam("end") String end)
    {
        try
        {
            String URI = getURI(all);
            ClientRequest client = new ClientRequest(headers.getHeaderString("email"), headers.getHeaderString("session_id"),
                    TransactionGenerator.generate(), URI, end, HTTPMethod.POST, jsonBytes, null);
            useThreadPool(client);
            Response.ResponseBuilder build = builder(client.getEmail(), client.getSession_id(), client.getTransaction_id());
            return build.build();
        } catch (InterruptedException e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server is busy." + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();

        } catch (Exception e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server Error" + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTwoPaths(byte[] jsonBytes, @Context HttpHeaders headers, @PathParam("all") String all, @PathParam("end") String end, @Context UriInfo uri_info)
    {
        try
        {
            String URI = getURI(all);
            ClientRequest client = new ClientRequest(headers.getHeaderString("email"), headers.getHeaderString("session_id"),
                    TransactionGenerator.generate(), URI, end, HTTPMethod.GET, jsonBytes, uri_info.getQueryParameters());
            useThreadPool(client);
            Response.ResponseBuilder build = builder(client.getEmail(), client.getSession_id(), client.getTransaction_id());
            return build.build();
        } catch (InterruptedException e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server is busy." + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();

        } catch (Exception e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server Error" + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();
        }
    }

    @Path("{third}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postThreePaths(byte[] jsonBytes, @Context HttpHeaders headers, @PathParam("all") String all, @PathParam("end") String end, @PathParam("third") String third)
    {

        try
        {
            String URI = getURI(all);
            ClientRequest client = new ClientRequest(headers.getHeaderString("email"), headers.getHeaderString("session_id"),
                    TransactionGenerator.generate(), URI, end + "/" + third, HTTPMethod.POST, jsonBytes, null);
            useThreadPool(client);
            Response.ResponseBuilder build = builder(client.getEmail(), client.getSession_id(), client.getTransaction_id());
            return build.build();
        } catch (InterruptedException e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server is busy." + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();

        } catch (Exception e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server Error" + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();
        }
    }

    @Path("{third}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThreePaths(byte[] jsonBytes, @Context HttpHeaders headers, @PathParam("all") String all, @PathParam("end") String end, @Context UriInfo uri_info, @PathParam("third") String third)
    {
        try
        {
            String URI = getURI(all);
            ClientRequest client = new ClientRequest(headers.getHeaderString("email"), headers.getHeaderString("session_id"),
                    TransactionGenerator.generate(), URI, end + "/" + third, HTTPMethod.GET, jsonBytes, uri_info.getQueryParameters());
            useThreadPool(client);
            Response.ResponseBuilder build = builder(client.getEmail(), client.getSession_id(), client.getTransaction_id());
            return build.build();
        } catch (InterruptedException e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server is busy." + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();

        } catch (Exception e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server Error" + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();
        }
    }
    @Path("{third}/{forth}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postFourPaths(byte[] jsonBytes, @Context HttpHeaders headers, @PathParam("all") String all, @PathParam("end") String end, @PathParam("third") String third, @PathParam("forth") String forth)
    {

        try
        {
            String URI = getURI(all);
            ClientRequest client = new ClientRequest(headers.getHeaderString("email"), headers.getHeaderString("session_id"),
                    TransactionGenerator.generate(), URI, end + "/" + third + "/" + forth, HTTPMethod.POST, jsonBytes, null);

            useThreadPool(client);
            Response.ResponseBuilder build = builder(client.getEmail(), client.getSession_id(), client.getTransaction_id());
            return build.build();
        } catch (InterruptedException e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server is busy." + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();

        } catch (Exception e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server Error" + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();
        }
    }

    @Path("{third}/{forth}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFourPaths(byte[] jsonBytes, @Context HttpHeaders headers, @PathParam("all") String all, @PathParam("end") String end, @Context UriInfo uri_info, @PathParam("third") String third, @PathParam("forth") String forth)
    {
        try
        {
            String URI = getURI(all);
            ClientRequest client = new ClientRequest(headers.getHeaderString("email"), headers.getHeaderString("session_id"),
                    TransactionGenerator.generate(), URI, end + "/" + third + "/" + forth, HTTPMethod.GET, jsonBytes, uri_info.getQueryParameters());
            useThreadPool(client);
            Response.ResponseBuilder build = builder(client.getEmail(), client.getSession_id(), client.getTransaction_id());
            return build.build();
        } catch (InterruptedException e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server is busy." + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();

        } catch (Exception e)
        {
            ReportResponse responseModel = new ReportResponse();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            ServiceLogger.LOGGER.info("Server Error" + e.getMessage());
            e.printStackTrace();
            return responseModel.buildResponse();
        }
    }

    private static void useThreadPool(ClientRequest client) throws InterruptedException
    {
        ServiceLogger.LOGGER.info("URI: " + client.getURI());
        ServiceLogger.LOGGER.info("Endpoint: " + client.getEndpoint());
        GatewayService.getThreadPool().putRequest(client);
    }

    private static String getURI(String path)
    {
        String[] split = path.split("/");
        return split[0];
    }

    public static SessionResponseModel checkSession(String email, String session_id) {
        SessionRequestModel requestModel = new SessionRequestModel(email, session_id);
        String servicePath = GatewayService.getIdmConfigs().getScheme() + GatewayService.getIdmConfigs().getHostName() + ":" +
                GatewayService.getIdmConfigs().getPort() + GatewayService.getIdmConfigs().getPath();
        String endpointPath = GatewayService.getIdmConfigs().getSessionPath();
        ServiceLogger.LOGGER.info("Building client... " + servicePath + endpointPath);
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
        return Util.modelMapper(jsonText, SessionResponseModel.class);

    }

    public static Response.ResponseBuilder builder(String email, String session_id, String transaction_id)
    {
        //ServiceLogger.LOGGER.info("Worker finished, building....");
        Response.ResponseBuilder build = Response.status(Response.Status.NO_CONTENT);
        build.header("message", "No Content");
        build.header("request_delay", GatewayService.getThreadConfigs().getRequestDelay());
        build.header("transaction_id", transaction_id);
        build.header("email", email);
        build.header("session_id", session_id);
        return build;
    }

}
