package edu.uci.ics.sidneyjt.service.billing.models.base;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.logger.ServiceLogger;

import javax.ws.rs.core.Response;

public abstract class ResponseModel
{
    @JsonIgnore
    private Result result;

    public ResponseModel() { }

    public ResponseModel(Result result)
    {
        this.result = result;
    }

    @JsonProperty("resultCode")
    public Integer getResultCode()
    {
        return result.getResultCode();
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return result.getMessage();
    }

    @JsonIgnore
    public Result getResult()
    {
        return result;
    }

    @JsonIgnore
    public void setResult(Result result)
    {
        ServiceLogger.LOGGER.info(result.getMessage());
        this.result = result;
    }

    @JsonIgnore
    public Response buildResponse()
    {
        ServiceLogger.LOGGER.info("Response being built with Result: " + result);

        if (result == null || result.getStatus() == Response.Status.INTERNAL_SERVER_ERROR)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        return Response.status(result.getStatus()).entity(this).build();
    }

    @JsonIgnore
    public Response buildResponse(String email, String session_id, String transaction_id){
        ServiceLogger.LOGGER.info("Response being built with Result: " + result);


        if (result == null || result.getStatus() == Response.Status.INTERNAL_SERVER_ERROR)
        {
            Response.ResponseBuilder build =  Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            build.header("email", email);
            build.header("session_id", session_id);
            build.header("transaction_id", transaction_id);
            return build.build();
        }

        Response.ResponseBuilder build = Response.status(result.getStatus()).entity(this);
        build.header("email", email);
        build.header("session_id", session_id);
        build.header("transaction_id", transaction_id);
        return build.build();
    }

}
